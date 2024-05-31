package com.example.tasktrack.fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.models.TaskModel
import com.example.domain.repository.TaskRepository
import com.example.domain.usecase.GetAllTasksUseCase
import com.example.domain.utils.AppResult
import com.example.tasktrack.di.AppContainer
import java.lang.Exception

class TaskViewModel(
    appContainer: AppContainer,
    private val taskRepository: TaskRepository = appContainer.provideRepository(),
    private val getAllTasksUseCase: GetAllTasksUseCase = GetAllTasksUseCase(taskRepository)
): ViewModel() {

    private val taskDataMutable = MutableLiveData<List<TaskModel>>()

    fun fetchData() {
        try {
            val result = getAllTasksUseCase.execute()
            if(result is AppResult.Success){
                result.data.let{
                    taskDataMutable.value = it
                }
            }
        } catch (e: Exception) {
            Log.e("task", e.message.toString())
        }
    }

}