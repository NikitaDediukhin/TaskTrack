package com.example.tasktrack.di

import android.app.Application
import com.example.data.mappers.TaskRepositoryMapper
import com.example.data.repository.TaskRepositoryImpl
import com.example.domain.repository.TaskRepository

class AppContainer(application: Application) {
    private val mapper: TaskRepositoryMapper = TaskRepositoryMapper()
    private val repository: TaskRepository = TaskRepositoryImpl(
        application = application,
        mapper = mapper)

    fun provideMapper(): TaskRepositoryMapper = mapper

    fun provideRepository(): TaskRepository = repository
}