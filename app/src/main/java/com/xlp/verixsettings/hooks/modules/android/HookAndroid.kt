package com.xlp.verixsettings.hooks.modules.android

import com.xlp.verixsettings.BuildConfig
import com.xlp.verixsettings.hooks.mPrefsMap
import com.xlp.verixsettings.utils.Init.TAG
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam


object HookAndroid {
    fun gameFps(lpparam: LoadPackageParam) {
        if (mPrefsMap.getBoolean("game_fps")) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookAndroid::gameFps")
            val targetClass = XposedHelpers.findClass(
                "com.android.server.wm.WindowState",
                lpparam.classLoader
            )
            hookGameFps(targetClass)
        }
    }

    fun forcedScreenCapture(lpparam: LoadPackageParam) {
        if (mPrefsMap.getBoolean("forced_screen_capture")) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookAndroid::forcedScreenCapture")
            val targetClass = XposedHelpers.findClass(
                "com.android.server.wm.WindowState",
                lpparam.classLoader
            )
            hookForcedScreenCapture(targetClass)
        }
    }

    private fun hookGameFps(clazz: Class<*>) {
        XposedHelpers.findAndHookMethod(
            clazz,
            "isFocused",
            XC_MethodReplacement.returnConstant(false)
        )
    }

    private fun hookForcedScreenCapture(clazz: Class<*>) {
        XposedHelpers.findAndHookMethod(
            clazz,
            "isSecureLocked",
            XC_MethodReplacement.returnConstant(false)
        )
    }
}