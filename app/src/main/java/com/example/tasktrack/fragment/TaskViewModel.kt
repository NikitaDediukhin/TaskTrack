package com.example.tasktrack.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.TaskModel
import com.example.domain.usecase.CreateTaskUseCase
import com.example.domain.usecase.DeleteTaskUseCase
import com.example.domain.usecase.EditTaskUseCase
import com.example.domain.usecase.GetAllTasksUseCase
import com.example.domain.usecase.MarkTaskUseCase
import com.example.domain.utils.AppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val markTaskUseCase: MarkTaskUseCase,
    private val editTaskUseCase: EditTaskUseCase
): ViewModel() {

    // LiveData for monitoring the list of uncompleted tasks
    private val uncompletedTasksMutable = MutableLiveData<List<TaskModel>>()
    val uncompletedTasks: LiveData<List<TaskModel>> = uncompletedTasksMutable

    // LiveData for monitoring the list of completed tasks
    private val completedTasksMutable = MutableLiveData<List<TaskModel>>()
    val completedTasks: LiveData<List<TaskModel>> = completedTasksMutable

    init {
        fetchData()
    }

    // Getting all tasks from the database
    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getAllTasksUseCase.execute()
                if(result is AppResult.Success){
                    result.data.let {
                        // Switching to the MainThread to update LiveData
                        withContext(Dispatchers.Main) {
                            uncompletedTasksMutable.value = it.filter { !it.competitionStatus }
                            completedTasksMutable.value = it.filter { it.competitionStatus }
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("apptask", e.message.toString())
                }
            }
        }
    }

    // Сreating a new task and adding it to the database
    fun createTask(task: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = createTaskUseCase.execute(task)
                if (result is AppResult.Success){
                    fetchData()
                    withContext(Dispatchers.Main) {
                        Log.w("apptask", "task created!")
                    }
                }
            } catch (e: Exception){
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

    fun markTask(taskModel: TaskModel, status: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = markTaskUseCase.execute(taskModel, status)
                if (result is AppResult.Success) {
                    fetchData()
                }
            } catch(e: Exception) {
                withContext(Dispatchers.Main){
                    Log.e("appTask", e.message.toString())
                }
            }
        }
    }

    fun editTask(taskModel: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = editTaskUseCase.execute(taskModel)
                if (result is AppResult.Success) {
                    fetchData()
                }
            } catch(e: Exception) {
                withContext(Dispatchers.Main){
                    Log.e("appTask", e.message.toString())
                }
            }
        }
    }

    // task sorting
    private fun sortTasks(tasks: List<TaskModel>, sortBy: String): List<TaskModel> {
        return when (sortBy) {
            "title_asc" -> tasks.sortedBy { it.title }
            "title_desc" -> tasks.sortedByDescending { it.title }
            "date_asc" -> tasks.sortedBy { it.creationDate }
            "date_desc" -> tasks.sortedByDescending { it.creationDate }
            else -> tasks
        }
    }

    fun sortUncompletedTasks(sortBy: String) {
        uncompletedTasksMutable.value = sortTasks(uncompletedTasksMutable.value ?: emptyList(), sortBy)
    }

    fun sortCompletedTasks(sortBy: String) {
        completedTasksMutable.value = sortTasks(completedTasksMutable.value ?: emptyList(), sortBy)
    }

}