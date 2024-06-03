package com.example.tasktrack.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.TaskModel
import com.example.tasktrack.adapters.TaskAdapter
import com.example.tasktrack.databinding.TasksFragmentBinding
import com.example.tasktrack.di.AppContainer
import com.example.tasktrack.utils.DialogManager
import java.util.Date

class TasksFragment: Fragment() {

    private lateinit var binding: TasksFragmentBinding
    private lateinit var rvTasks: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var viewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TasksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // инициализация компонентов View
        init()

        // анонимный наблюдатель за изменениями в LiveData taskDataLive в viewModel
        viewModel.taskLiveData.observe(viewLifecycleOwner) {
            // вызов метода при изменении данных, добавить новые задачи в RecyclerView
            taskAdapter.submitList(it)
        }

        // Первоначальный запрос данных у viewModel
        viewModel.fetchData()
    }

    override fun onResume() {
        super.onResume()
        // обновление UI с текущими задачами
        viewModel.fetchData()
    }

    private fun init(){
        // Плавающая кнопка (добавить задачу)
        val btnAddTask: View = binding.btnAddTask
        btnAddTask.setOnClickListener{
            showAddTaskDialog()
        }

        // RecyclerView & Adapter
        rvTasks = binding.rvTasks
        taskAdapter = TaskAdapter()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvTasks.layoutManager = layoutManager
        rvTasks.adapter = taskAdapter

        // Зависимости
        val appContainer = AppContainer(requireActivity().application)

        // ViewModel
        viewModel = TaskViewModel(appContainer)
    }

    // вывести диалоговое окно для добавления задачи
    private fun showAddTaskDialog() {
        context?.let { context ->
            DialogManager.addTaskDialog(context, object : DialogManager.Listener {
                // добавить задачу в БД
                override fun onClick(title: String, description: String, dueDate: Date) {
                    try {
                        viewModel.createTask(
                            title = title,
                            description = description,
                            changeDate = Date(),
                            dueDate = dueDate
                        )
                    } catch (e: Exception){
                        Log.e("task", e.message.toString())
                    }
                }
            })
        }
    }

}