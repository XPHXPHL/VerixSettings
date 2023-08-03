package com.xlp.verixsettings.utils

import android.annotation.SuppressLint
import java.io.DataOutputStream

/**
 * 执行 Shell 命令
 * @param command Shell 命令
 */
fun execShell(command: String) {
    try {
        val p = Runtime.getRuntime().exec("su")
        val outputStream = p.outputStream
        val dataOutputStream = DataOutputStream(outputStream)
        dataOutputStream.writeBytes(command)
        dataOutputStream.flush()
        dataOutputStream.close()
        outputStream.close()
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}

@SuppressLint("PrivateApi")
fun setProp(prop: String, state: String) {
    execShell("setprop $prop $state")
}

@SuppressLint("PrivateApi")
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun getProp(mKey: String): String =
    Class.forName("android.os.SystemProperties").getMethod("get", String::class.java).invoke(Class.forName("android.os.SystemProperties"), mKey).toString()

@SuppressLint("PrivateApi")
fun getProp(mKey: String, defaultValue: Boolean): Boolean =
    Class.forName("android.os.SystemProperties").getMethod("getBoolean", String::class.java, Boolean::class.javaPrimitiveType)
        .invoke(Class.forName("android.os.SystemProperties"), mKey, defaultValue) as Boolean