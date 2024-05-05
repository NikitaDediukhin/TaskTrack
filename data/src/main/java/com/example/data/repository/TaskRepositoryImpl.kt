package com.example.data.repository

import android.app.Application
import com.example.data.database.AppDatabase
import com.example.data.mappers.TaskRepositoryMapper
import com.example.domain.models.TaskModel
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.AppResult

class TaskRepositoryImpl(
    application: Application,
    private val mapper: TaskRepositoryMapper
): TaskRepository {

    private val db: AppDatabase = AppDatabase.getInstance(application.applicationContext)

    override fun getAllTasks(): AppResult<List<TaskModel>> {
        return try {
            AppResult.Success(db.taskDAO().getAll().map { mapper.toTaskModel(it) })
        } catch (e: Exception){
            AppResult.Error(e.message ?: "Unknown error")
        }
    }

    override fun getTaskById(id: Int): AppResult<TaskModel> {
        return try {
            AppResult.Success(mapper.toTaskModel(db.taskDAO().getById(id)))
        } catch (e: Exception){
            AppResult.Error(e.message ?: "Unknown error")
        }
    }

    override fun createTask(task: TaskModel): AppResult<Int> {
        return try {
            AppResult.Success(db.taskDAO().insert(mapper.toTaskEntity(task)))
        } catch (e: Exception){
            AppResult.Error(e.message ?: "Unknown error")
        }
    }

    override fun updateTask(task: TaskModel): AppResult<Int> {
        return try {
            AppResult.Success(db.taskDAO().update(mapper.toTaskEntity(task)))
        } catch (e: Exception){
            AppResult.Error(e.message ?: "Unknown error")
        }
    }

    override fun deleteTask(task: TaskModel): AppResult<Int> {
        return try {
            AppResult.Success(db.taskDAO().delete(mapper.toTaskEntity(task)))
        } catch (e: Exception){
            AppResult.Error(e.message ?: "Unknown error")
        }
    }
}