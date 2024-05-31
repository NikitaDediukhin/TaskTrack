package com.example.domain.usecase

import com.example.domain.models.TaskModel
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.AppResult

class CreateTaskUseCase(
    private val repository: TaskRepository
) {
    fun execute(task: TaskModel): AppResult<Long> {
        return try {
            repository.createTask(task)
        } catch (e: Exception) {
            AppResult.Error(e.message ?: "Unknown error")
        }
    }
}