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

        init()

        viewModel.taskDataLive.observe(viewLifecycleOwner) {
            showTasks(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }

    private fun showTasks(tasks: List<TaskModel>){
        taskAdapter.setDataTasks(tasks)
    }

    private fun showAddTaskDialog() {
        context?.let { context ->
            DialogManager.addTaskDialog(context, object : DialogManager.Listener {
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

    private fun init(){
        val btnAddTask: View = binding.btnAddTask

        btnAddTask.setOnClickListener{
            showAddTaskDialog()
        }

        rvTasks = binding.rvTasks
        taskAdapter = TaskAdapter()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvTasks.layoutManager = layoutManager
        rvTasks.adapter = taskAdapter

        val appContainer = AppContainer(requireActivity().application)
        viewModel = TaskViewModel(appContainer)
    }

}