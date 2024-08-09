package com.example.tasktrack.fragment

interface ViewPagerFragment {
    fun searchTask(query: String)
    fun sortTasks(sortBy: String)
    fun clearFilter()
}