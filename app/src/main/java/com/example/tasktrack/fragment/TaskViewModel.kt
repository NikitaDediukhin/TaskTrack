package com.example.tasktrack.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.TaskModel
import com.example.domain.repository.TaskRepository
import com.example.domain.usecase.CreateTaskUseCase
import com.example.domain.usecase.DeleteTaskUseCase
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
    private val createTaskUseCase: CreateTaskUseCase = CreateTaskUseCase(taskRepository),
    private val deleteTaskUseCase: DeleteTaskUseCase = DeleteTaskUseCase(taskRepository)
): ViewModel() {

    // LiveData для наблюдения за списком задач
    private val taskLiveDataMutable = MutableLiveData<List<TaskModel>>()
    val taskLiveData: LiveData<List<TaskModel>> = taskLiveDataMutable

    // получение всех задач из БД
    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getAllTasksUseCase.execute()
                if(result is AppResult.Success){
                    result.data.let{
                        // Переключение на главный поток для обновления LiveData
                        withContext(Dispatchers.Main) {
                            taskLiveDataMutable.value = it
                        }
                    }
                }
            } catch (e: Exception) {
                // Переключение на главный поток для логирования ошибок
                withContext(Dispatchers.Main){
                    Log.e("apptask", e.message.toString())
                }
            }
        }
    }

    // создание новой задачи и добавления её в БД
    fun createTask(title: String, description: String, changeDate: Date, dueDate: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = createTaskUseCase.execute(
                    TaskModel(
                        id = 0,
                        title = title,
                        description = description,
                        creationDate = changeDate,
                        dueDate = dueDate,
                        competitionStatus = false
                    )
                )
                if (result is AppResult.Success){
                    // получение актуальных данных из базы данных
                    fetchData()
                    // Переключение на главный поток для логирования успешного создания задачи
                    withContext(Dispatchers.Main) {
                        Log.w("apptask", "task created!")
                    }
                }
            } catch (e: Exception){
                // Переключение на главный поток для логирования ошибок
                withContext(Dispatchers.Main){
                    Log.e("apptask", e.message.toString())
                }
            }
        }
    }

    fun deleteTask(taskModel: TaskModel){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = deleteTaskUseCase.execute(taskModel)
                if (result is AppResult.Success) {
                    fetchData()
                }
            } catch(e: Exception){
                withContext(Dispatchers.Main){
                    Log.e("appTask", e.message.toString())
                }
            }
        }
    }



}