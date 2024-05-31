package com.example.domain.repository

import com.example.domain.models.TaskModel
import com.example.domain.utils.AppResult

interface TaskRepository {

    fun getAllTasks(): AppResult<List<TaskModel>>

    fun getTaskById(id: Int): AppResult<TaskModel>

    fun createTask(task: TaskModel): AppResult<Long>

    fun updateTask(task: TaskModel): AppResult<Int>

    fun deleteTask(task: TaskModel): AppResult<Int>

}