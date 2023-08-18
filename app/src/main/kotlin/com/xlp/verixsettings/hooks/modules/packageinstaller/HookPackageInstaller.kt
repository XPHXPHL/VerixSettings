package com.xlp.verixsettings.hooks.modules.packageinstaller

import android.annotation.SuppressLint
import android.content.Context
import com.xlp.verixsettings.BuildConfig
import com.xlp.verixsettings.utils.Init.TAG
import com.xlp.verixsettings.utils.getBoolean
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

object HookPackageInstaller {
    fun silentInstall(lpparam: LoadPackageParam) {
        if (getBoolean("silent_install", false)) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookPackageInstaller::silentInstall")
            XposedHelpers.findAndHookMethod(
                "com.meizu.permissioncommon.AppInfoUtil",
                lpparam.classLoader,
                "isSystemApp",
                Context::class.java,
                String::class.java,
                XC_MethodReplacement.returnConstant(true)
            )
        }
    }

    fun skipVirusCheckTime(lpparam: LoadPackageParam) {
        if (getBoolean("skip_virus_check_time", false)) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookPackageInstaller::skipVirusCheckTime")
            XposedHelpers.findAndHookMethod(
                "com.android.packageinstaller.FlymePackageInstallerActivity",
                lpparam.classLoader,
                "lambda\$setVirusCheckTime\$12",
                object : XC_MethodReplacement() {
                    @SuppressLint("WrongConstant")
                    override fun replaceHookedMethod(param: MethodHookParam) {
                        val mHandler = XposedHelpers.getObjectField(param.thisObject, "mHandler")
                        XposedHelpers.callMethod(mHandler, "sendEmptyMessage", 5)
                    }
                }
            )
        }
    }
}