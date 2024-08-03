package com.example.tasktrack.di

import android.content.Context
import com.example.data.mappers.TaskRepositoryMapper
import com.example.data.repository.TaskRepositoryImpl
import com.example.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideTaskRepositoryMapper(): TaskRepositoryMapper {
        return TaskRepositoryMapper()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        @ApplicationContext context: Context,
        mapper: TaskRepositoryMapper
    ): TaskRepository {
        return TaskRepositoryImpl(context = context, mapper = mapper)
    }
}