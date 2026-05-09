package com.fomalhaut.kernix.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.fomalhaut.kernix.ShizukuManager

@Composable
fun MainScreen(){
    var hasPermission: Boolean by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        hasPermission = ShizukuManager.CheckPermission()
    }
    Button(onClick = { ShizukuManager.requestPermission(1001) }){
        if(hasPermission){
            Text("Permission Granted")
        }else{
            Text("Request Access")
        }
    }
}
@Preview
@Composable
fun MainPreview(){
    MainScreen()
}