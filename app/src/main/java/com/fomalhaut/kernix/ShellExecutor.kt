@file:Suppress("DEPRECATION")

package com.fomalhaut.kernix

import android.util.Log
import rikka.shizuku.Shizuku.newProcess
import java.io.BufferedReader
import java.io.InputStreamReader

fun ShellExecutor(command: String): String {
    return try {
        val remoteProcess = newProcess(arrayOf("sh", "-c", command), null, null)

        val outputReader = BufferedReader(InputStreamReader(remoteProcess.inputStream))
        val errorReader = BufferedReader(InputStreamReader(remoteProcess.errorStream))

        val output = outputReader.readText()
        val error = errorReader.readText()

        if (error.isNotEmpty()) {
            Log.e("ShellExecutor", "Command: $command | Error: $error")
        }

        if (output.isNotEmpty()) {
            Log.d("ShellExecutor", "Command: $command | Output: ${output.take(100)}...")
        }

        output
    } catch (e: Exception) {
        Log.e("ShellExecutor", "Failed to execute command: $command", e)
        ""
    }
}