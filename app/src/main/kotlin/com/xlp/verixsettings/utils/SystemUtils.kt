package com.xlp.verixsettings.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.XposedHelpers

@SuppressLint("WrongConstant")
object SystemUtils {
    fun vibratorUtils (param: MethodHookParam,effectId:Int){
        val mContext =
            XposedHelpers.getObjectField(param.thisObject, "mContext") as Context
        val mVibrator = mContext.getSystemService("vibrator") as Vibrator
        mVibrator.vibrate(VibrationEffect.createPredefined(effectId))
    }
}