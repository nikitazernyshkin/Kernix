package com.fomalhaut.kernix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fomalhaut.kernix.ui.screens.MainScreen
import com.fomalhaut.kernix.ui.theme.KernixTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KernixTheme {
                MainScreen()
            }
        }
    }
}