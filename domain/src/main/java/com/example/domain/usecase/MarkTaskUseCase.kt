package com.example.domain.usecase

import com.example.domain.models.TaskModel
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.AppResult

class MarkTaskUseCase(
    private val repository: TaskRepository
) {
    suspend fun execute(task: TaskModel, status: Boolean): AppResult<Int> {
        return try {
            repository.markTaskById(task.id, status)
        } catch (e: Exception) {
            AppResult.Error(e.message ?: "Unknown error")
        }
    }
}