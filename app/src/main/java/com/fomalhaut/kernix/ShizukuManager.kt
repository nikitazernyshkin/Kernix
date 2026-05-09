package com.fomalhaut.kernix

import android.content.pm.PackageManager
import rikka.shizuku.Shizuku
object ShizukuManager {
    fun ShizukuPing(): Boolean {
        return try {
            Shizuku.pingBinder() != null
        } catch (_: Exception) {
            false
        }
    }
    fun CheckPermission(): Boolean{
        return try{
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        } catch(_: Exception){
            false
        }
    }
    fun requestPermission(code: Int){
        try {
            Shizuku.requestPermission(code)
        } catch(_: Exception){
            println("Shizuku error")
        }
    }
}