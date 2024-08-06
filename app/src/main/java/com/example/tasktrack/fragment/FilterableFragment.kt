package com.example.tasktrack.fragment

interface FilterableFragment {
    fun searchTask(query: String)
    fun sortTasks(sortBy: String)
}