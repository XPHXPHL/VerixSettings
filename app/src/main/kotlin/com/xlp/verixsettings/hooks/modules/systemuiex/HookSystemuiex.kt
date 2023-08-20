package com.xlp.verixsettings.hooks.modules.systemuiex

import com.xlp.verixsettings.BuildConfig
import com.xlp.verixsettings.utils.Init.TAG
import com.xlp.verixsettings.utils.getBoolean
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

object HookSystemuiex {
    fun forcedScreenCapture2(lpparam: LoadPackageParam) {
        if (getBoolean("forced_screen_capture", false)) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookSystemuiex::forcedScreenCapture2")
            XposedHelpers.findAndHookMethod(
                "android.view.SurfaceControl\$ScreenshotHardwareBuffer",
                lpparam.classLoader,
                "containsSecureLayers",
                XC_MethodReplacement.returnConstant(false)
            )
        }
    }
}