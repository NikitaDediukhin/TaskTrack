package com.example.domain.usecase

import com.example.domain.models.TaskModel
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.AppResult

class EditTaskUseCase (
    private val repository: TaskRepository
) {
    suspend fun execute(taskModel: TaskModel): AppResult<Int> {
        return try {
            repository.updateTask(taskModel)
        } catch (e: Exception) {
            AppResult.Error(e.message ?: "Unknown error")
        }
    }
}