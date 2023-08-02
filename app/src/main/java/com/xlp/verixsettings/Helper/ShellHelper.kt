package com.xlp.verixsettings.Helper

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

fun ShellHelper(command: String) {
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
                Log.i(Init.TAG, "${line}")
            }
            val errorReader = BufferedReader(InputStreamReader(process.errorStream))
            var errorLine: String?
            while (errorReader.readLine().also { errorLine = it } != null) {
                Log.e(Init.TAG, "${errorLine}")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
