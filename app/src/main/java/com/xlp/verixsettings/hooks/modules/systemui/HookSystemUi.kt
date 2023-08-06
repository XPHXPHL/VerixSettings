package com.xlp.verixsettings.hooks.modules.systemui

import android.annotation.SuppressLint
import android.content.Context
import android.os.IBinder
import android.os.PowerManager
import com.xlp.verixsettings.BuildConfig
import com.xlp.verixsettings.hooks.mPrefsMap
import com.xlp.verixsettings.utils.Init.TAG
import com.xlp.verixsettings.utils.SystemUtils.vibratorUtils
import com.xlp.verixsettings.utils.writeFileNode
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

object HookSystemUi {
    fun blur(lpparam: LoadPackageParam) {
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
    }

    fun faceVib(lpparam: LoadPackageParam) {
        if (mPrefsMap.getBoolean("face_vibrator")) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking face unlocked successfully vibrate")
            val targetClass = XposedHelpers.findClass(
                "com.flyme.systemui.facerecognition.FaceRecognitionAnimationView",
                lpparam.classLoader
            )
            hookFaceVib(targetClass)
        }
    }

    fun backVib(lpparam: LoadPackageParam) {
        if (mPrefsMap.getBoolean("back_vibrator")) {
            val targetClass = XposedHelpers.findClass(
                "com.flyme.systemui.navigationbar.gestural.EdgeBackView",
                lpparam.classLoader
            )
            hookBackVib(targetClass)
        }

    }

    fun fingerVib(lpparam: LoadPackageParam) {
        if (mPrefsMap.getBoolean("finger_vibrator")) {
            val targetClass = XposedHelpers.findClass(
                "com.android.keyguard.KeyguardUpdateMonitor",
                lpparam.classLoader
            )
            hookFingerVib(targetClass)
        }
    }

    fun fingerUnlock(lpparam: LoadPackageParam) {
        if (mPrefsMap.getBoolean("finger_unlock")) {
            val targetClass = XposedHelpers.findClass(
                "com.android.keyguard.KeyguardUpdateMonitor",
                lpparam.classLoader
            )
            hookFingerUnlock(targetClass)
        }
    }

    fun batteryProtect(lpparam: LoadPackageParam) {
        if (mPrefsMap.getBoolean("battery_protect")) {
            val targetClass = XposedHelpers.findClass(
                "com.android.flyme.statusbar.battery.FlymeBatteryMeterView",
                lpparam.classLoader
            )
            hookBatteryProtect(targetClass)
        }
    }

    fun appShade(lpparam: LoadPackageParam){
        if (mPrefsMap.getBoolean("app_shade")){
            val targetClass = XposedHelpers.findClass(
                "com.android.wm.shell.startingsurface.StartingWindowController",
                lpparam.classLoader
            )
            hookAppShade(targetClass)
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
                override fun afterHookedMethod(param: MethodHookParam) {
                    super.afterHookedMethod(param)
                    vibratorUtils(param,31021)
                }
            }
        )
    }

    private fun hookFaceVib(clazz: Class<*>) {
        XposedHelpers.findAndHookMethod(
            clazz,
            "startSuccessAnimation",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    super.afterHookedMethod(param)
                    vibratorUtils(param,31021)
                }
            }
        )
    }

    private fun hookBackVib(clazz: Class<*>) {
        XposedHelpers.findAndHookMethod(
            clazz,
            "triggerBack",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    super.afterHookedMethod(param)
                    vibratorUtils(param,31021)

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
                    val mContext =
                        XposedHelpers.getObjectField(param?.thisObject, "mContext") as Context
                    val mPowerManager = mContext.getSystemService("power") as PowerManager
                    param?.result = !(mPowerManager.isScreenOn)
                }
            }

        )
    }

    private fun hookBatteryProtect(clazz: Class<*>) {
        XposedHelpers.findAndHookMethod(
            clazz,
            "onBatteryLevelChanged",
            Int::class.java,
            Boolean::class.java,
            Boolean::class.java,
            Boolean::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    val level = param?.args?.get(0) as Int
                    val pluggedIn = param.args?.get(1) as Boolean
                    if (pluggedIn && level >= 91) {
                        writeFileNode("/sys/class/power_supply/battery/battery_charging_enabled","0")
                    } else {
                        writeFileNode("/sys/class/power_supply/battery/battery_charging_enabled","1")
                    }
                }
            }
        )
    }

    private fun hookAppShade(clazz: Class<*>){
        XposedHelpers.findAndHookMethod(
            clazz,
            "lambda\$addStartingWindow$0\$com-android-wm-shell-startingsurface-StartingWindowController",
            "android.window.StartingWindowInfo",
            IBinder::class.java,
        )
        object : XC_MethodHook(){
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                val windowInfo = param.args[0]
                val appToken = param.args[1] as IBinder
                val mStartingSurfaceDrawer =
                    XposedHelpers.getObjectField(param.thisObject, "mStartingSurfaceDrawer")
                XposedHelpers.callMethod(
                    mStartingSurfaceDrawer,
                    "addSplashScreenStartingWindow",
                    windowInfo,
                    appToken,
                    0
                )
            }
        }
    }
}