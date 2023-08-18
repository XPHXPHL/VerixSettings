package com.xlp.verixsettings.hooks.modules.systemui

import android.annotation.SuppressLint
import android.content.Context
import android.os.IBinder
import android.os.PowerManager
import com.xlp.verixsettings.BuildConfig
import com.xlp.verixsettings.utils.Init.TAG
import com.xlp.verixsettings.utils.SystemUtils.vibratorUtils
import com.xlp.verixsettings.utils.getBoolean
import com.xlp.verixsettings.utils.writeFileNode
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

object HookSystemUi {
    fun blur(lpparam: LoadPackageParam) {
        if (getBoolean("blur_enabled", true)) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookSystemUi::blur")
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

    fun faceVib(lpparam: LoadPackageParam) {
        if (getBoolean("face_vibrator", true)) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookSystemUi::faceVib")
            XposedHelpers.findAndHookMethod(
                "com.flyme.systemui.facerecognition.FaceRecognitionAnimationView",
                lpparam.classLoader,
                "startSuccessAnimation",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)
                        vibratorUtils(param, 31021)
                    }
                }
            )
        }
    }

    fun backVib(lpparam: LoadPackageParam) {
        if (getBoolean("back_vibrator", true)) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookSystemUi::backVib")
            XposedHelpers.findAndHookMethod(
                "com.flyme.systemui.navigationbar.gestural.EdgeBackView",
                lpparam.classLoader,
                "triggerBack",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)
                        vibratorUtils(param, 31021)
                    }
                }
            )
        }
    }

    fun fingerVib(lpparam: LoadPackageParam) {
        if (getBoolean("finger_vibrator", true)) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookSystemUi::fingerVib")
            XposedHelpers.findAndHookMethod(
                "com.android.keyguard.KeyguardUpdateMonitor",
                lpparam.classLoader,
                "onFingerprintAuthenticated",
                Int::class.java,
                Boolean::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)
                        vibratorUtils(param, 31021)
                    }
                }
            )
        }
    }

    fun fingerUnlock(lpparam: LoadPackageParam) {
        if (getBoolean("finger_unlock", true)) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookSystemUi::fingerUnlock")
            XposedHelpers.findAndHookMethod(
                "com.android.keyguard.KeyguardUpdateMonitor",
                lpparam.classLoader,
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
    }

    fun clipboardEditor(lpparam: LoadPackageParam) {
        if (getBoolean("clipboard_editor", true)) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookSystemUi::clipboardEditor")
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.clipboardoverlay.ClipboardListener",
                lpparam.classLoader,
                "start",
                object : XC_MethodReplacement() {
                    @Throws(Throwable::class)
                    override fun replaceHookedMethod(param: MethodHookParam): Any? {
                        val mClipboardManager =
                            XposedHelpers.getObjectField(param.thisObject, "mClipboardManager")
                        XposedHelpers.callMethod(
                            mClipboardManager,
                            "addPrimaryClipChangedListener",
                            param.thisObject
                        )
                        return null
                    }
                }
            )
        }
    }

    fun batteryProtect(lpparam: LoadPackageParam) {
        if (getBoolean("battery_protect", true)) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookSystemUi::batteryProtect")
            XposedHelpers.findAndHookMethod(
                "com.android.flyme.statusbar.battery.FlymeBatteryMeterView",
                lpparam.classLoader,
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
                            writeFileNode(
                                "/sys/class/power_supply/battery/battery_charging_enabled",
                                "0"
                            )
                        } else {
                            writeFileNode(
                                "/sys/class/power_supply/battery/battery_charging_enabled",
                                "1"
                            )
                        }
                    }
                }
            )
        }
    }

    fun appShade(lpparam: LoadPackageParam) {
        if (getBoolean("app_shade", true)) {
            if (BuildConfig.DEBUG) XposedBridge.log("$TAG: Hooking HookSystemUi::appShade")
            XposedHelpers.findAndHookMethod(
                "com.android.wm.shell.startingsurface.StartingWindowController",
                lpparam.classLoader,
                "lambda\$addStartingWindow$0\$com-android-wm-shell-startingsurface-StartingWindowController",
                "android.window.StartingWindowInfo",
                IBinder::class.java,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
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
            )
        }
    }
}