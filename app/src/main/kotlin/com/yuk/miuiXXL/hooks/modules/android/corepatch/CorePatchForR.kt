package com.yuk.miuiXXL.hooks.modules.android.corepatch

import android.app.AndroidAppHelper
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import com.github.kyuubiran.ezxhelper.Log
import com.yuk.miuiXXL.utils.prefs
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.IXposedHookZygoteInit.StartupParam
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import java.lang.reflect.Member
import java.lang.reflect.Method
import java.security.cert.Certificate
import java.security.cert.X509Certificate
import java.util.zip.ZipEntry

open class CorePatchForR : XposedHelper(), IXposedHookLoadPackage, IXposedHookZygoteInit {
    override fun handleLoadPackage(loadPackageParam: LoadPackageParam) {
        // 允许降级
        findAndHookMethod(
            "com.android.server.pm.PackageManagerService",
            loadPackageParam.classLoader,
            "checkDowngrade",
            "com.android.server.pm.parsing.pkg.AndroidPackage",
            "android.content.pm.PackageInfoLite",
            ReturnConstant(prefs(), "downgrade", null)
        )
        hookAllMethods(
            "com.android.server.pm.PackageManagerServiceUtils", loadPackageParam.classLoader, "isDowngradePermitted",
            ReturnConstant(prefs(), "downgrade", true)
        )

        // apk内文件修改后 digest校验会失败
        hookAllMethods(
            "android.util.jar.StrictJarVerifier", loadPackageParam.classLoader, "verifyMessageDigest", ReturnConstant(prefs(), "authcreak", true)
        )
        hookAllMethods("android.util.jar.StrictJarVerifier", loadPackageParam.classLoader, "verify", ReturnConstant(prefs(), "authcreak", true))
        hookAllMethods("java.security.MessageDigest", loadPackageParam.classLoader, "isEqual", ReturnConstant(prefs(), "authcreak", true))

        // Targeting R+ (version " + Build.VERSION_CODES.R + " and above) requires"
        // + " the resources.arsc of installed APKs to be stored uncompressed"
        // + " and aligned on a 4-byte boundary
        // target >=30 的情况下 resources.arsc 必须是未压缩的且4K对齐
        hookAllMethods(
            "android.content.res.AssetManager", loadPackageParam.classLoader, "containsAllocatedTable", ReturnConstant(prefs(), "authcreak", false)
        )

        // No signature found in package of version " + minSignatureSchemeVersion
        // + " or newer for package " + apkPath
        findAndHookMethod(
            "android.util.apk.ApkSignatureVerifier",
            loadPackageParam.classLoader,
            "getMinimumSignatureSchemeVersionForTargetSdk",
            Int::class.javaPrimitiveType,
            ReturnConstant(prefs(), "authcreak", 0)
        )

        findAndHookMethod(
            "com.android.apksig.ApkVerifier",
            loadPackageParam.classLoader,
            "getMinimumSignatureSchemeVersionForTargetSdk",
            Int::class.javaPrimitiveType,
            ReturnConstant(prefs(), "authcreak", 0)
        )

        // Package " + packageName + " signatures do not match previously installed version; ignoring!"
        // public boolean checkCapability(String sha256String, @CertCapabilities int flags) {
        // public boolean checkCapability(SigningDetails oldDetails, @CertCapabilities int flags)
        hookAllMethods("android.content.pm.PackageParser", loadPackageParam.classLoader, "checkCapability", object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                // Don't handle PERMISSION (grant SIGNATURE permissions to pkgs with this cert)
                // Or applications will have all privileged permissions
                // https://cs.android.com/android/platform/superproject/+/master:frameworks/base/core/java/android/content/pm/PackageParser.java;l=5947?q=CertCapabilities
                if (prefs().getBoolean("authcreak", true)) {
                    if (param.args[1] as Int != 4) param.result = true

                }
            }
        })

        // 当verifyV1Signature抛出转换异常时，替换一个签名作为返回值
        // 如果用户已安装apk，并且其定义了私有权限，则安装时会因签名与模块内硬编码的不一致而被拒绝。尝试从待安装apk中获取签名。如果其中apk的签名和已安装的一致（只动了内容）就没有问题。此策略可能有潜在的安全隐患。
        val pkc = XposedHelpers.findClass("sun.security.pkcs.PKCS7", loadPackageParam.classLoader)
        val constructor = XposedHelpers.findConstructorExact(pkc, ByteArray::class.java)
        constructor.isAccessible = true
        val aSVClass = XposedHelpers.findClass("android.util.apk.ApkSignatureVerifier", loadPackageParam.classLoader)
        val sJarClass = XposedHelpers.findClass("android.util.jar.StrictJarFile", loadPackageParam.classLoader)
        val constructorExact =
            XposedHelpers.findConstructorExact(sJarClass, String::class.java, Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType)
        constructorExact.isAccessible = true
        val signingDetails = getSigningDetails(loadPackageParam.classLoader)
        val findConstructorExact = XposedHelpers.findConstructorExact(signingDetails, Array<Signature>::class.java, Integer.TYPE)
        findConstructorExact.isAccessible = true
        val packageParserException = XposedHelpers.findClass("android.content.pm.PackageParser.PackageParserException", loadPackageParam.classLoader)
        val error = XposedHelpers.findField(packageParserException, "error")
        error.isAccessible = true
        val signingDetailsArgs = arrayOfNulls<Any>(2)
        signingDetailsArgs[1] = 1
        val parseResult = XposedHelpers.findClassIfExists("android.content.pm.parsing.result.ParseResult", loadPackageParam.classLoader)
        hookAllMethods("android.util.jar.StrictJarVerifier", loadPackageParam.classLoader, "verifyBytes", object : XC_MethodHook() {
            public override fun afterHookedMethod(param: MethodHookParam) {
                super.afterHookedMethod(param)
                if (prefs().getBoolean("digestCreak", true)) {
                    if (!prefs().getBoolean("UsePreSig", false)) {
                        val block = constructor.newInstance(param.args[0])
                        val infos = XposedHelpers.callMethod(block, "getSignerInfos") as Array<Any>
                        val info = infos[0]
                        val verifiedSignerCertChain = XposedHelpers.callMethod(info, "getCertificateChain", block) as List<X509Certificate>
                        param.result = verifiedSignerCertChain.toTypedArray()
                    }
                }
            }
        })
        hookAllMethods("android.util.apk.ApkSignatureVerifier", loadPackageParam.classLoader, "verifyV1Signature", object : XC_MethodHook() {
            public override fun afterHookedMethod(methodHookParam: MethodHookParam) {
                if (prefs().getBoolean("authcreak", true)) {
                    val throwable = methodHookParam.throwable
                    var parseErr: Int? = null
                    if (parseResult != null && (methodHookParam.method as Method).returnType == parseResult) {
                        val result = methodHookParam.result
                        if (XposedHelpers.callMethod(result, "isError") as Boolean) {
                            parseErr = XposedHelpers.callMethod(result, "getErrorCode") as Int
                        }
                    }
                    if (throwable != null || parseErr != null) {
                        var lastSigs: Array<Signature?>? = null
                        try {
                            if (prefs().getBoolean("UsePreSig", false)) {
                                val pkgM = AndroidAppHelper.currentApplication().packageManager
                                if (pkgM != null) {
                                    val pI: PackageInfo? = if (parseErr != null) {
                                        pkgM.getPackageArchiveInfo((methodHookParam.args[1] as String), 0)
                                    } else {
                                        pkgM.getPackageArchiveInfo((methodHookParam.args[0] as String), 0)
                                    }
                                    val pkgI = pkgM.getPackageInfo(pI!!.packageName, PackageManager.GET_SIGNATURES)
                                    lastSigs = pkgI.signatures
                                }
                            }
                        } catch (ignored: Throwable) {
                        }
                        try {
                            if (lastSigs == null && prefs().getBoolean("digestCreak", true)) {
                                val origJarFile = constructorExact.newInstance(methodHookParam.args[if (parseErr == null) 0 else 1], true, false)
                                val manifestEntry = XposedHelpers.callMethod(origJarFile, "findEntry", "AndroidManifest.xml") as ZipEntry
                                val lastCerts: Array<Array<Certificate>> = if (parseErr != null) {
                                    XposedHelpers.callMethod(
                                        XposedHelpers.callStaticMethod(
                                            aSVClass, "loadCertificates", methodHookParam.args[0], origJarFile, manifestEntry
                                        ), "getResult"
                                    ) as Array<Array<Certificate>>
                                } else {
                                    XposedHelpers.callStaticMethod(
                                        aSVClass, "loadCertificates", origJarFile, manifestEntry
                                    ) as Array<Array<Certificate>>
                                }
                                lastSigs = XposedHelpers.callStaticMethod(aSVClass, "convertToSignatures", lastCerts as Any) as Array<Signature?>
                            }
                        } catch (ignored: Throwable) {
                        }
                        if (lastSigs != null) {
                            signingDetailsArgs[0] = lastSigs
                        } else {
                            signingDetailsArgs[0] = arrayOf(Signature(SIGNATURE))
                        }
                        var newInstance = findConstructorExact.newInstance(*signingDetailsArgs)

                        //修复 java.lang.ClassCastException: Cannot cast android.content.pm.PackageParser$SigningDetails to android.util.apk.ApkSignatureVerifier$SigningDetailsWithDigests
                        val signingDetailsWithDigests = XposedHelpers.findClassIfExists(
                            "android.util.apk.ApkSignatureVerifier.SigningDetailsWithDigests", loadPackageParam.classLoader
                        )
                        if (signingDetailsWithDigests != null) {
                            val signingDetailsWithDigestsConstructorExact =
                                XposedHelpers.findConstructorExact(signingDetailsWithDigests, signingDetails, MutableMap::class.java)
                            signingDetailsWithDigestsConstructorExact.isAccessible = true
                            newInstance = signingDetailsWithDigestsConstructorExact.newInstance(newInstance, null)
                        }
                        if (throwable != null) {
                            val cause = throwable.cause
                            if (throwable.javaClass == packageParserException) {
                                if (error.getInt(throwable) == -103) {
                                    methodHookParam.result = newInstance
                                }
                            }
                            if (cause != null && cause.javaClass == packageParserException) {
                                if (error.getInt(cause) == -103) {
                                    methodHookParam.result = newInstance
                                }
                            }
                        }
                        if (parseErr != null && parseErr == -103) {
                            val input = methodHookParam.args[0]
                            XposedHelpers.callMethod(input, "reset")
                            methodHookParam.result = XposedHelpers.callMethod(input, "success", newInstance)
                        }
                    }
                }
            }
        })


        //New package has a different signature
        //处理覆盖安装但签名不一致
        hookAllMethods(signingDetails, "checkCapability", object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                // Don't handle PERMISSION (grant SIGNATURE permissions to pkgs with this cert)
                // Or applications will have all privileged permissions
                // https://cs.android.com/android/platform/superproject/+/master:frameworks/base/core/java/android/content/pm/PackageParser.java;l=5947?q=CertCapabilities
                if (param.args[1] as Int != 4 && prefs().getBoolean("digestCreak", true)) param.result = true
            }
        })
        // if app is system app, allow to use hidden api, even if app not using a system signature
        findAndHookMethod(
            "android.content.pm.ApplicationInfo",
            loadPackageParam.classLoader,
            "isPackageWhitelistedForHiddenApis",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    super.beforeHookedMethod(param)
                    if (prefs().getBoolean("digestCreak", true)) {
                        val info = param.thisObject as ApplicationInfo
                        if (info.flags and ApplicationInfo.FLAG_SYSTEM != 0 || info.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0) param.result =
                            true
                    }
                }
            })
        val utilClass: Class<*> = XposedHelpers.findClass("com.android.server.pm.PackageManagerServiceUtils", loadPackageParam.classLoader)
        for (m in utilClass.declaredMethods) {
            if ("verifySignatures" == m.name) {
                try {
                    XposedBridge::class.java.getDeclaredMethod("deoptimizeMethod", Member::class.java).invoke(null, m)
                } catch (e: Throwable) {
                    Log.ex("deoptimizing failed", e)
                }
            }
        }
    }

    open fun getSigningDetails(classLoader: ClassLoader?): Class<*> {
        return XposedHelpers.findClass("android.content.pm.PackageParser.SigningDetails", classLoader)
    }

    override fun initZygote(startupParam: StartupParam) {
        hookAllMethods("android.content.pm.PackageParser", null, "getApkSigningVersion", XC_MethodReplacement.returnConstant(1))
        hookAllConstructors("android.util.jar.StrictJarVerifier", object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                if (prefs().getBoolean("enhancedMode", false)) {
                    super.beforeHookedMethod(param)
                    param.args[3] = false
                }
            }
        })
    }

}
