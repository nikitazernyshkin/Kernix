@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.fomalhaut.kernix.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fomalhaut.kernix.ProcessInfo
import com.fomalhaut.kernix.viewModel.ProcessViewModel

@Composable
fun MainScreen(viewModel: ProcessViewModel){
    val searchQuery by viewModel.queryState.collectAsState()
    var selectedProcess by remember { mutableStateOf<ProcessInfo?>(null) }
    val processes by viewModel.state.collectAsState()
    if(selectedProcess != null){
        AlertDialog(
            title = {Text("${selectedProcess?.name}")},
            onDismissRequest = { selectedProcess = null },
            text = {
                Column {
                    Text(" PID: ${selectedProcess?.pid}")
                    Text(" USERNAME: ${selectedProcess?.username}")
                }
            },
            confirmButton = {Button(
                onClick = {selectedProcess = null}
            ){
                Text("OK")
            }}
        )
    }
    Scaffold { padding ->
        LazyColumn(
            Modifier.padding(padding)
        ) {item{
            Text(
                text = "Kernix",
                fontSize = 30.sp,
                color = Color.Green,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {newText ->
                    viewModel.onSearchQueryChanged(newText)
                },
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                placeholder = {Text(
                    text = "Search...",
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )}
            )
            Text("> status: monitoring active", fontSize = 12.sp, color = Color.Gray, fontFamily = FontFamily.Monospace)
            Text(text = "Processes:", fontSize = 15.sp, fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold)
        }; items(processes) { process ->
            var expanded by remember { mutableStateOf(false) }
            Box(
                Modifier.combinedClickable(
                    onLongClick = {
                        expanded = true
                    },
                    onClick = {
                        selectedProcess = process
                    }
                )
            ) {
                Text(process.name, fontFamily = FontFamily.Monospace)
                DropdownMenu(
                    expanded,
                    {expanded = false}
                ){
                    DropdownMenuItem(
                        onClick = { expanded = false
                            viewModel.killProcess(process.name)
                        },
                        text = {Text("[KILL]")}
                    )
                }
            }
        }
        }
    }
}
