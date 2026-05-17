package com.fomalhaut.kernix

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.fomalhaut.kernix.ui.screens.MainScreen
import com.fomalhaut.kernix.ui.theme.KernixTheme
import com.fomalhaut.kernix.viewModel.ProcessViewModel
import rikka.shizuku.Shizuku

class MainActivity : ComponentActivity() {
    val viewModel: ProcessViewModel by viewModels()

    val check = ShizukuManager.CheckPermission()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Shizuku.addRequestPermissionResultListener { _, grantResult ->
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                viewModel.refreshProcesses()
            }
        }
        if(check) {
            viewModel.refreshProcesses()
        } else{
            ShizukuManager.requestPermission(100)
        }
        enableEdgeToEdge()
        setContent {
            KernixTheme {
                MainScreen(viewModel)
            }
        }
    }
}