package com.xlp.verixsettings.hooks.modules.android

import com.xlp.verixsettings.hooks.mPrefsMap
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam


object HookAndroid {
    fun gameFps(lpparam: LoadPackageParam) {
        if (mPrefsMap.getBoolean("game_fps")) {
            val targetClass = XposedHelpers.findClass(
                "com.android.server.wm.WindowState",
                lpparam.classLoader
            )
            hookGameFps(targetClass)
        }
    }
    private fun hookGameFps(clazz: Class<*>){
        XposedHelpers.findAndHookMethod(
            clazz,
            "isFocused",
            XC_MethodReplacement.returnConstant(false)
        )
    }
}