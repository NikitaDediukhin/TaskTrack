package com.example.tasktrack.di

import com.example.domain.repository.TaskRepository
import com.example.domain.usecase.CreateTaskUseCase
import com.example.domain.usecase.DeleteTaskUseCase
import com.example.domain.usecase.EditTaskUseCase
import com.example.domain.usecase.GetAllTasksUseCase
import com.example.domain.usecase.MarkTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideCreateTaskUseCase(repository: TaskRepository): CreateTaskUseCase {
        return CreateTaskUseCase(repository = repository)
    }

    @Provides
    fun provideDeleteTaskUseCase(repository: TaskRepository): DeleteTaskUseCase {
        return DeleteTaskUseCase(repository = repository)
    }

    @Provides
    fun provideEditTaskUseCase(repository: TaskRepository): EditTaskUseCase {
        return EditTaskUseCase(repository = repository)
    }

    @Provides
    fun provideGetAllTasksUseCase(repository: TaskRepository): GetAllTasksUseCase {
        return GetAllTasksUseCase(repository = repository)
    }

    @Provides
    fun provideMarkTaskUseCase(repository: TaskRepository): MarkTaskUseCase {
        return MarkTaskUseCase(repository = repository)
    }

}