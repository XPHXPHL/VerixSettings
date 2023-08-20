package com.xlp.verixsettings.hooks.modules.settings

import android.annotation.SuppressLint
import com.xlp.verixsettings.BuildConfig
import com.xlp.verixsettings.utils.Init.TAG
import com.xlp.verixsettings.utils.getBoolean
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam


object HookSettings {
    fun cipherDiskVib(lpparam: LoadPackageParam) {
        if (getBoolean("cipher_disk_vibrator", false)) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookSettings::cipherDiskVib")
            XposedHelpers.findAndHookMethod(
                "com.meizu.settings.widget.LockDigitView",
                lpparam.classLoader,
                "detectAndAddHit",
                Float::class.java,
                Float::class.java,
                Boolean::class.java,
                object : XC_MethodHook() {
                    @SuppressLint("WrongConstant")
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        super.beforeHookedMethod(param)
                        val targetObject = param?.thisObject
                        XposedHelpers.setBooleanField(targetObject, "mEnableHapticFeedback", true)
                    }
                }
            )
        }
    }
    fun flymeLTPO(lpparam: LoadPackageParam){
        if (getBoolean("flyme_LTPO", false)){
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookSettings::flymeLTPO")
            var hook: XC_MethodHook.Unhook? = null
            XposedHelpers.findAndHookMethod(
                "com.meizu.settings.display.FlymeRefreshRateAndResolutionFragment",
                lpparam.classLoader,
                "isLtpoSupported",
                XC_MethodReplacement.returnConstant(true)
            )
            hook = XposedHelpers.findAndHookMethod(
                "com.meizu.settings.utils.MzUtils",
                lpparam.classLoader,
                "checkProductModel",
                String::class.java,
                try {
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            super.beforeHookedMethod(param)
                            val str = "M2391"
                            param.args[0] = str
                        }
                        override fun afterHookedMethod(param: MethodHookParam) {
                            super.afterHookedMethod(param)
                            hook?.unhook()
                        }
                    }
                }catch (e:Exception){
                    XposedBridge.log("$TAG:$e")
                }
            )
        }
    }
}