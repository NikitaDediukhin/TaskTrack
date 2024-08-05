package com.example.tasktrack.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.TaskModel
import com.example.tasktrack.activity.MainActivity
import com.example.tasktrack.adapters.UncompletedTasksAdapter
import com.example.tasktrack.databinding.UncompletedTasksFragmentBinding
import com.example.tasktrack.utils.DialogManager
import java.util.Date

class UncompletedTasksFragment: Fragment(), SearchableFragment {

    private lateinit var binding: UncompletedTasksFragmentBinding
    private lateinit var taskAdapter: UncompletedTasksAdapter
    private lateinit var viewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UncompletedTasksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = (activity as MainActivity).vm

        init()

        viewModel.uncompletedTasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }
    }

    private fun init() {
        // RecyclerView & Adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        taskAdapter = UncompletedTasksAdapter(
            onDeleteTask = { task -> deleteTask(task) },
            onMarkTask = { task, b -> markTask(task, b) },
            onEditTask = { task -> showEditTaskDialog(task) }
        )

        binding.rvTasks.layoutManager = layoutManager
        binding.rvTasks.adapter = taskAdapter
    }

    private fun showEditTaskDialog(task: TaskModel) {
        context?.let {context ->
            DialogManager.editTaskDialog(context, task, object : DialogManager.EditTaskListener {
                override fun onClick(title: String, description: String, dueDate: Date) {
                    try {
                        val updatedTask = task.copy(
                            title = title,
                            description = description,
                            dueDate = dueDate
                        )
                        viewModel.editTask(updatedTask)
                    } catch (e: Exception){
                        Log.e("task", e.message.toString())
                    }
                }
            })

        }
    }

    private fun deleteTask(taskModel: TaskModel) {
        viewModel.deleteTask(taskModel)
    }

    private fun markTask(taskModel: TaskModel, status: Boolean) {
        viewModel.markTask(taskModel, status)
    }

    override fun searchTask(query: String) {
        viewModel.uncompletedTasks.observe(viewLifecycleOwner) {
            val searchTasks = it.filter { taskModel ->
                taskModel.title.contains(query, true) || taskModel.description.contains(query, true)
            }
            taskAdapter.submitList(searchTasks)
        }
    }

}