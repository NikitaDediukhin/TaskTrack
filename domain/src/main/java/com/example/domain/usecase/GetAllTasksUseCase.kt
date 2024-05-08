package com.example.domain.usecase

import com.example.domain.models.TaskModel
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.AppResult

class GetAllTasksUseCase(
    private val repository: TaskRepository
) {
    fun execute(): AppResult<List<TaskModel>> {
        return try {
            repository.getAllTasks()
        } catch (e: Exception) {
            AppResult.Error(e.message ?: "Unknown Error")
        }
    }
}