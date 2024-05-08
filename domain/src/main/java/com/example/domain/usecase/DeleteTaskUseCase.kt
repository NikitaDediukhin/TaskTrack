package com.example.domain.usecase

import com.example.domain.models.TaskModel
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.AppResult

class DeleteTaskUseCase(
    private val repository: TaskRepository
) {
    fun execute(task: TaskModel): AppResult<Int> {
        return try {
            repository.deleteTask(task)
        } catch (e: Exception) {
            AppResult.Error(e.message ?: "Unknown error")
        }
    }
}