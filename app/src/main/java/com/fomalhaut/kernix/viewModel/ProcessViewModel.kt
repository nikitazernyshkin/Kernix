package com.fomalhaut.kernix.viewModel

import android.app.AppOpsManager
import android.app.Application
import android.app.usage.UsageStatsManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fomalhaut.kernix.ProcessInfo
import com.fomalhaut.kernix.ProcessParser
import com.fomalhaut.kernix.ShellExecutor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku
import kotlin.collections.listOf

class ProcessViewModel(application: Application) : AndroidViewModel(application){
    private val _allProcesses = MutableStateFlow(listOf<ProcessInfo>())
    private val searchQuery = MutableStateFlow<String>("")

    val queryState = searchQuery.asStateFlow()

    val state = _allProcesses.combine(searchQuery) { processes, query ->
        if(query.isBlank()){
            processes
        } else{
            processes.filter { it.name.contains(query, ignoreCase = true) }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        listOf()
    )

    fun hasUsageStatsPermission(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
    fun refreshProcesses(){
        viewModelScope.launch {
            _allProcesses.value =
                if(Shizuku.pingBinder()){
                    ProcessParser(ShellExecutor("ps -A")).distinctBy { it.name }.sortedByDescending { it.name.contains(".") }
                } else{
                    val usm = getApplication<Application>().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

                    if (hasUsageStatsPermission(getApplication())){
                        val stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, System.currentTimeMillis() - 60000, System.currentTimeMillis())
                        stats.map { ProcessInfo("", 0, it.packageName) }.distinctBy { it.name }
                    }else{
                        emptyList()
                    }
                }
        }
    }
    fun killProcess(name: String) {
        viewModelScope.launch {
            if (Shizuku.pingBinder()) {
                ShellExecutor("am force-stop $name")
                kotlinx.coroutines.delay(500)
                refreshProcesses()
            }
        }
    }
    fun onSearchQueryChanged(newQuery: String){
        searchQuery.value = newQuery
    }
}

