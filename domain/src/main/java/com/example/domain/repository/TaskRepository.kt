package com.example.domain.repository

import com.example.domain.models.TaskModel
import com.example.domain.utils.AppResult

interface TaskRepository {

    suspend fun getAllTasks(): AppResult<List<TaskModel>>

    suspend fun getTaskById(id: Int): AppResult<TaskModel>

    suspend fun createTask(task: TaskModel): AppResult<Long>

    suspend fun updateTask(task: TaskModel): AppResult<Int>

    suspend fun deleteTask(task: TaskModel): AppResult<Int>

}