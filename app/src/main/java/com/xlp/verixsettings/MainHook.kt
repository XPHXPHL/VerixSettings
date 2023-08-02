package com.xlp.verixsettings

import android.annotation.SuppressLint
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == "com.android.systemui") {
            XposedBridge.log("Hooking target app: ${lpparam.packageName}")
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
            val targetClass4 = XposedHelpers.findClass(
                "com.android.keyguard.KeyguardUpdateMonitor",
                lpparam.classLoader
            )
            val targetClass5 = XposedHelpers.findClass(
                "com.flyme.systemui.navigationbar.gestural.EdgeBackView",
                lpparam.classLoader
            )
            hookBlur(targetClass)
            hookBlur(targetClass2)
            hookBlur(targetClass3)
            hookFingerVib(targetClass4)
            hookFaceVib(targetClass4)
            hookBackVib(targetClass5)
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
