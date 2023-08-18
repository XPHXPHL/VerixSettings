package com.xlp.verixsettings.hooks.modules.android

import com.xlp.verixsettings.BuildConfig
import com.xlp.verixsettings.utils.Init.TAG
import com.xlp.verixsettings.utils.getBoolean
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam


object HookAndroid {
    fun forcedScreenCapture(lpparam: LoadPackageParam) {
        if (getBoolean("forced_screen_capture",true)) {
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
        if (getBoolean("game_fps",true)) {
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