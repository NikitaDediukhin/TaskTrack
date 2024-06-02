package com.example.tasktrack.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.TaskModel
import com.example.domain.repository.TaskRepository
import com.example.domain.usecase.CreateTaskUseCase
import com.example.domain.usecase.GetAllTasksUseCase
import com.example.domain.utils.AppResult
import com.example.tasktrack.di.AppContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import kotlin.Exception

class TaskViewModel(
    appContainer: AppContainer,
    private val taskRepository: TaskRepository = appContainer.provideRepository(),
    private val getAllTasksUseCase: GetAllTasksUseCase = GetAllTasksUseCase(taskRepository),
    private val createTaskUseCase: CreateTaskUseCase = CreateTaskUseCase(taskRepository)
): ViewModel() {

    private val taskLiveDataMutable = MutableLiveData<List<TaskModel>>()
    val taskDataLive: LiveData<List<TaskModel>> = taskLiveDataMutable

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getAllTasksUseCase.execute()
                if(result is AppResult.Success){
                    result.data.let{
                        withContext(Dispatchers.Main) {
                            taskLiveDataMutable.value = it
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("apptask", e.message.toString())
            }
        }
    }

    fun createTask(title: String, description: String, changeDate: Date, dueDate: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = createTaskUseCase.execute(
                    TaskModel(
                        title = title,
                        description = description,
                        creationDate = changeDate,
                        dueDate = dueDate,
                        competitionStatus = false
                    )
                )
                if (result is AppResult.Success){
                    withContext(Dispatchers.Main) {
                        Log.w("apptask", "task created!")
                    }
                }
            } catch (e: Exception){
                Log.e("apptask", e.message.toString())
            }
        }
    }



}