package com.xlp.verixsettings.hooks.modules.systemuiex

import com.xlp.verixsettings.hooks.mPrefsMap
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

object HookSystemuiex {
    fun forcedScreenCapture2(lpparam: LoadPackageParam) {
        if(mPrefsMap.getBoolean("forced_screen_capture")){
            val targetClass = XposedHelpers.findClass(
                "android.view.SurfaceControl\$ScreenshotHardwareBuffer",
                lpparam.classLoader
            )
            hookForcedScreenCapture(targetClass)
        }
    }
    private fun hookForcedScreenCapture(clazz: Class<*>){
        XposedHelpers.findAndHookMethod(
            clazz,
            "containsSecureLayers",
            XC_MethodReplacement.returnConstant(false)
        )
    }
}