package com.xlp.verixsettings

import com.xlp.verixsettings.hooks.modules.settings.HookSettings.hookCipherDiskVib
import com.xlp.verixsettings.hooks.modules.systemui.HookSystemUi.batteryProtect
import com.xlp.verixsettings.hooks.modules.systemui.HookSystemUi.fingerUnlock
import com.xlp.verixsettings.hooks.modules.systemui.HookSystemUi.hookBackVib
import com.xlp.verixsettings.hooks.modules.systemui.HookSystemUi.hookBlur
import com.xlp.verixsettings.hooks.modules.systemui.HookSystemUi.hookFaceVib
import com.xlp.verixsettings.hooks.modules.systemui.HookSystemUi.hookFingerVib
import com.xlp.verixsettings.utils.Init.TAG
import com.xlp.verixsettings.utils.PrefsHelpers.mAppModulePkg
import com.xlp.verixsettings.utils.PrefsHelpers.mPrefsName
import com.xlp.verixsettings.utils.PrefsMap
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

var mPrefsMap = PrefsMap<String, Any>()

class MainHook : IXposedHookLoadPackage, IXposedHookZygoteInit {
    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        if (mPrefsMap.size == 0) {
            var mXSharedPreferences: XSharedPreferences? = null
            try {
                mXSharedPreferences = XSharedPreferences(mAppModulePkg, mPrefsName)
                mXSharedPreferences.makeWorldReadable()
            } catch (t: Throwable) {
                XposedBridge.log(t)
            }
            val allPrefs = mXSharedPreferences!!.all
            if (allPrefs == null || allPrefs.isEmpty()) {
                if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Cannot read module's SharedPreferences, some mods might not work!")
            } else {
                mPrefsMap.putAll(allPrefs)
            }
        }
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        when (lpparam?.packageName){
            "com.android.systemui" -> {
                hookBlur(lpparam)
                hookBackVib(lpparam)
                hookFaceVib(lpparam)
                hookFingerVib(lpparam)
                fingerUnlock(lpparam)
                batteryProtect(lpparam)
            }
            "com.android.settings" -> {
                hookCipherDiskVib(lpparam)
            }
        }
    }
}
