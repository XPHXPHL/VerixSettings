package com.xlp.verixsettings.hooks.modules.settings

import android.annotation.SuppressLint
import com.xlp.verixsettings.BuildConfig
import com.xlp.verixsettings.utils.Init.TAG
import com.xlp.verixsettings.utils.getBoolean
import de.robv.android.xposed.XC_MethodHook
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
}