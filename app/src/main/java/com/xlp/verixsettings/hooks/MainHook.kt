package com.xlp.verixsettings.hooks

import com.xlp.verixsettings.BuildConfig
import com.xlp.verixsettings.hooks.modules.android.HookAndroid.forcedScreenCapture
import com.xlp.verixsettings.hooks.modules.android.HookAndroid.gameFps
import com.xlp.verixsettings.hooks.modules.packageinstaller.HookPackageInstaller.silentInstall
import com.xlp.verixsettings.hooks.modules.packageinstaller.HookPackageInstaller.skipVirusCheckTime
import com.xlp.verixsettings.hooks.modules.settings.HookSettings.cipherDiskVib
import com.xlp.verixsettings.hooks.modules.systemui.HookSystemUi.appShade
import com.xlp.verixsettings.hooks.modules.systemui.HookSystemUi.backVib
import com.xlp.verixsettings.hooks.modules.systemui.HookSystemUi.batteryProtect
import com.xlp.verixsettings.hooks.modules.systemui.HookSystemUi.blur
import com.xlp.verixsettings.hooks.modules.systemui.HookSystemUi.clipboardEditor
import com.xlp.verixsettings.hooks.modules.systemui.HookSystemUi.faceVib
import com.xlp.verixsettings.hooks.modules.systemui.HookSystemUi.fingerUnlock
import com.xlp.verixsettings.hooks.modules.systemui.HookSystemUi.fingerVib
import com.xlp.verixsettings.hooks.modules.systemuiex.HookSystemuiex.forcedScreenCapture2
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
        if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hook Starting!")
        when (lpparam?.packageName) {
            "com.android.systemui" -> {
                if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hook Systemui succeed!")
                blur(lpparam)
                backVib(lpparam)
                faceVib(lpparam)
                fingerVib(lpparam)
                fingerUnlock(lpparam)
                batteryProtect(lpparam)
                appShade(lpparam)
                clipboardEditor(lpparam)

            }

            "com.android.settings" -> {
                if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hook Settings succeed!")
                cipherDiskVib(lpparam)
            }
            "android" -> {
                if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hook android succeed!")
                gameFps(lpparam)
                forcedScreenCapture(lpparam)
            }

            "com.flyme.systemuiex" -> {
                if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hook systemuiex succeed!")
                forcedScreenCapture2(lpparam)
            }

            "com.android.packageinstaller" -> {
                if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hook packageinstaller succeed!")
                silentInstall(lpparam)
                skipVirusCheckTime(lpparam)
            }
        }
    }
}
