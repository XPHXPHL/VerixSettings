package com.xlp.verixsettings.utils

import android.annotation.SuppressLint
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader


fun execShell(command: String): String {
    var process: Process? = null
    var reader: BufferedReader? = null
    var `is`: InputStreamReader? = null
    var os: DataOutputStream? = null
    return try {
        process = Runtime.getRuntime().exec("su")
        `is` = InputStreamReader(process.inputStream)
        reader = BufferedReader(`is`)
        os = DataOutputStream(process.outputStream)
        os.writeBytes(command.trimIndent())
        os.writeBytes("\nexit\n")
        os.flush()
        var read: Int
        val buffer = CharArray(4096)
        val output = StringBuilder()
        while (reader.read(buffer).also { read = it } > 0) output.appendRange(buffer, 0, read)
        process.waitFor()
        output.toString()
    } catch (e: IOException) {
        throw RuntimeException(e)
    } catch (e: InterruptedException) {
        throw RuntimeException(e)
    } finally {
        try {
            os?.close()
            `is`?.close()
            reader?.close()
            process?.destroy()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun execShell(commands: Array<String>): String {
    val stringBuilder = java.lang.StringBuilder()
    for (command in commands) {
        stringBuilder.append(execShell(command))
        stringBuilder.append("\n")
    }
    return stringBuilder.toString()
}
fun writeFileNode(path: String, data: String): Boolean {
    var result: Boolean
    var fos: FileOutputStream? = null
    return try {
        try {
            fos = FileOutputStream(path)
            fos.write(data.toByteArray(charset("US-ASCII")))
            result = true
            try {
                fos.close()
            } catch (e: IOException) {
            }
        } catch (e2: IOException) {
            StringBuilder()
            e2.printStackTrace()
            if (fos != null) {
                try {
                    fos.close()
                } catch (e3: IOException) {
                }
            }
            result = false
        }
        StringBuilder()
        result
    } catch (th: Throwable) {
        if (fos != null) {
            try {
                fos.close()
            } catch (e4: IOException) {
            }
        }
        throw th
    }
}

@SuppressLint("PrivateApi")
fun setProp(prop: String, state: String) {
    execShell("setprop $prop $state")
}

@SuppressLint("PrivateApi")
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun getProp(mKey: String): String =
    Class.forName("android.os.SystemProperties").getMethod("get", String::class.java)
        .invoke(Class.forName("android.os.SystemProperties"), mKey).toString()

@SuppressLint("PrivateApi")
fun getProp(mKey: String, defaultValue: Boolean): Boolean =
    Class.forName("android.os.SystemProperties")
        .getMethod("getBoolean", String::class.java, Boolean::class.javaPrimitiveType)
        .invoke(Class.forName("android.os.SystemProperties"), mKey, defaultValue) as Boolean