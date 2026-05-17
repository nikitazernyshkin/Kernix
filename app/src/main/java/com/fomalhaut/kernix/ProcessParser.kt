package com.fomalhaut.kernix

fun ProcessParser(process: String): List<ProcessInfo> {
    val list = mutableListOf<ProcessInfo>()
    val lines = process.lines().drop(1)
    for (line in lines) {
        val columns = line.trim().split(Regex("\\s+"))
        if (columns.size >= 8) {
            list.add(
                ProcessInfo(
                    columns[0],
                    columns[1].toLongOrNull() ?: 0,
                    columns.last()
                )
            )
        }
    }
    return list

}
