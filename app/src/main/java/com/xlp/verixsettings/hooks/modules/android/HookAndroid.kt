package com.xlp.verixsettings.hooks.modules.android

import com.xlp.verixsettings.BuildConfig
import com.xlp.verixsettings.hooks.mPrefsMap
import com.xlp.verixsettings.utils.Init.TAG
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam


object HookAndroid {
    fun forcedScreenCapture(lpparam: LoadPackageParam) {
        if (mPrefsMap.getBoolean("forced_screen_capture")) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookAndroid::forcedScreenCapture")
            XposedHelpers.findAndHookMethod(
                "com.android.server.wm.WindowState",
                lpparam.classLoader,
                "isSecureLocked",
                XC_MethodReplacement.returnConstant(false)
            )
        }
    }

    fun gameFps(lpparam: LoadPackageParam) {
        if (mPrefsMap.getBoolean("game_fps")) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookAndroid::gameFps")
            XposedHelpers.findAndHookMethod(
                "com.android.server.wm.WindowState",
                lpparam.classLoader,
                "isFocused",
                XC_MethodReplacement.returnConstant(false)
            )
        }
    }
}