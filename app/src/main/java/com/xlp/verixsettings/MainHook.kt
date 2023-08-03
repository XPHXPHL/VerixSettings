package com.xlp.verixsettings

import android.annotation.SuppressLint
import android.content.Context
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import com.xlp.verixsettings.utils.Init.TAG
import com.xlp.verixsettings.utils.PrefsHelpers.mAppModulePkg
import com.xlp.verixsettings.utils.PrefsHelpers.mPrefsName
import com.xlp.verixsettings.utils.PrefsMap
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
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

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == "com.android.systemui") {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking target app: ${lpparam.packageName}")
            if (mPrefsMap.getBoolean("blur_enabled")) {
                if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking Blur")
                val targetClass = XposedHelpers.findClass(
                    "com.android.systemui.statusbar.notification.row.ActivatableNotificationView",
                    lpparam.classLoader
                )
                val targetClass2 = XposedHelpers.findClass(
                    "com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout",
                    lpparam.classLoader
                )
                val targetClass3 = XposedHelpers.findClass(
                    "com.android.systemui.media.MediaCarouseTransitionLayout",
                    lpparam.classLoader
                )
                hookBlur(targetClass)
                hookBlur(targetClass2)
                hookBlur(targetClass3)
            }

            if (mPrefsMap.getBoolean("face_vibrator")) {
                if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking face unlocked successfully vibrate")
                val targetClass = XposedHelpers.findClass(
                    "com.android.keyguard.KeyguardUpdateMonitor",
                    lpparam.classLoader
                )
                hookFaceVib(targetClass)
            }

            if (mPrefsMap.getBoolean("finger_vibrator")) {
                val targetClass = XposedHelpers.findClass(
                    "com.android.keyguard.KeyguardUpdateMonitor",
                    lpparam.classLoader
                )
                hookFingerVib(targetClass)
            }

            if (mPrefsMap.getBoolean("back_vibrator")) {
                val targetClass = XposedHelpers.findClass(
                    "com.flyme.systemui.navigationbar.gestural.EdgeBackView",
                    lpparam.classLoader
                )
                hookBackVib(targetClass)
            }

            if (mPrefsMap.getBoolean("finger_unlock")) {
                if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking Finger_unlock is")
                val targetClass = XposedHelpers.findClass(
                    "com.android.keyguard.KeyguardUpdateMonitor",
                    lpparam.classLoader
                )
                hookFingerUnlock(targetClass)
            }
        }
    }
}

private fun hookBlur(clazz: Class<*>) {
    XposedHelpers.findAndHookMethod(
        clazz,
        "isSupportBlur",
        object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                param.result = true
            }
        }
    )
}

private fun hookFingerVib(clazz: Class<*>) {
    XposedHelpers.findAndHookMethod(
        clazz,
        "onFingerprintAuthenticated",
        Int::class.java,
        Boolean::class.java,
        object : XC_MethodHook() {
            @SuppressLint("WrongConstant")
            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                val mContext =
                    XposedHelpers.getObjectField(param?.thisObject, "mContext") as Context
                val mVibrator = mContext.getSystemService("vibrator") as Vibrator
                mVibrator.vibrate(VibrationEffect.createPredefined(31021))
            }
        }
    )
}

private fun hookFaceVib(clazz: Class<*>) {
    XposedHelpers.findAndHookMethod(
        clazz,
        "onFaceRecognitionSucceeded",
        Boolean::class.java,
        object : XC_MethodHook() {
            @SuppressLint("WrongConstant")
            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                val mContext =
                    XposedHelpers.getObjectField(param?.thisObject, "mContext") as Context
                val mVibrator = mContext.getSystemService("vibrator") as Vibrator
                mVibrator.vibrate(VibrationEffect.createPredefined(31021))
            }
        }
    )
}

private fun hookBackVib(clazz: Class<*>) {
    XposedHelpers.findAndHookMethod(
        clazz,
        "triggerBack",
        object : XC_MethodHook() {
            @SuppressLint("WrongConstant")
            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                val mContext =
                    XposedHelpers.getObjectField(param?.thisObject, "mContext") as Context
                val mVibrator = mContext.getSystemService("vibrator") as Vibrator
                mVibrator.vibrate(VibrationEffect.createPredefined(31021))
            }
        }
    )
}

private fun hookFingerUnlock(clazz: Class<*>) {
    XposedHelpers.findAndHookMethod(
        clazz,
        "isFingerprintDisabled",
        Int::class.java,
        object : XC_MethodHook() {
            @Suppress("DEPRECATION")
            @SuppressLint("WrongConstant")
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                val mContext = XposedHelpers.getObjectField(param?.thisObject, "mContext") as Context
                val mPowerManager = mContext.getSystemService("power") as PowerManager
                param?.result = !(mPowerManager.isScreenOn)
            }
        }

    )
}