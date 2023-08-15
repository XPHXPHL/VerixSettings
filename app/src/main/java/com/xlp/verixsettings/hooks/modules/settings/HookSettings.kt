package com.xlp.verixsettings.hooks.modules.settings

import android.annotation.SuppressLint
import com.xlp.verixsettings.BuildConfig
import com.xlp.verixsettings.hooks.mPrefsMap
import com.xlp.verixsettings.utils.Init.TAG
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

object HookSettings {

    fun cipherDiskVib(lpparam: LoadPackageParam) {
        if (mPrefsMap.getBoolean("cipher_disk_vibrator")) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookSettings::cipherDiskVib")
            val targetClass = XposedHelpers.findClass(
                "com.meizu.settings.widget.LockDigitView",
                lpparam.classLoader
            )
            hookCipherDiskVib(targetClass)
        }
    }

    private fun hookCipherDiskVib(clazz: Class<*>) {
        XposedHelpers.findAndHookMethod(
            clazz,
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