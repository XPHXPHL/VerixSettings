package com.xlp.verixsettings.utils

import android.annotation.SuppressLint
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

class AppUtils {
    fun shellHelper(command: String) {
        try {
            //val process = ProcessBuilder("su", "-c", command)
            val process = ProcessBuilder(command)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()
            if (Init.DebugMode) {
                val outputReader = BufferedReader(InputStreamReader(process.inputStream))
                var line: String?
                while (outputReader.readLine().also { line = it } != null) {
                    Log.i(Init.TAG, "$line")
                }
                val errorReader = BufferedReader(InputStreamReader(process.errorStream))
                var errorLine: String?
                while (errorReader.readLine().also { errorLine = it } != null) {
                    Log.e(Init.TAG, "$errorLine")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("PrivateApi")
    fun setProp(prop: String, state: String) {
        shellHelper("setprop $prop $state")
    }

    @SuppressLint("PrivateApi")
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getProp(mKey: String): String =
        Class.forName("android.os.SystemProperties").getMethod("get", String::class.java).invoke(Class.forName("android.os.SystemProperties"), mKey).toString()

    @SuppressLint("PrivateApi")
    fun getProp(mKey: String, defaultValue: Boolean): Boolean =
        Class.forName("android.os.SystemProperties").getMethod("getBoolean", String::class.java, Boolean::class.javaPrimitiveType)
            .invoke(Class.forName("android.os.SystemProperties"), mKey, defaultValue) as Boolean
}