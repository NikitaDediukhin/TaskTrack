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
import com.example.tasktrack.adapters.IncompletedTasksAdapter
import com.example.tasktrack.databinding.IncompletedTasksFragmentBinding
import com.example.tasktrack.utils.DialogManager
import java.util.Date

class IncompletedTasksFragment: Fragment(), FilterableFragment {

    private lateinit var binding: IncompletedTasksFragmentBinding
    private lateinit var taskAdapter: IncompletedTasksAdapter
    private lateinit var viewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = IncompletedTasksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = (activity as MainActivity).vm

        init()

        viewModel.incompletedTasks.observe(viewLifecycleOwner) {
            binding.rvIncompletedTasks.scrollToPosition(0)
            taskAdapter.submitList(it) {
                binding.rvIncompletedTasks.post {
                    binding.rvIncompletedTasks.scrollToPosition(0)
                }
            }
        }
    }

    private fun init() {
        // RecyclerView & Adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        taskAdapter = IncompletedTasksAdapter(
            context = requireContext(),
            onDeleteTask = { task -> deleteTask(task) },
            onMarkTask = { task, b -> markTask(task, b) },
            onEditTask = { task -> showEditTaskDialog(task) }
        )

        binding.rvIncompletedTasks.layoutManager = layoutManager
        binding.rvIncompletedTasks.adapter = taskAdapter
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
        viewModel.incompletedTasks.observe(viewLifecycleOwner) {
            val searchTasks = it.filter { taskModel ->
                taskModel.title.contains(query, true) || taskModel.description.contains(query, true)
            }
            taskAdapter.submitList(searchTasks)
        }
    }

    // Task sorting
    override fun sortTasks(sortBy: String) {
        viewModel.sortIncompletedTasks(sortBy).apply {
            binding.rvIncompletedTasks.post{
                binding.rvIncompletedTasks.scrollToPosition(0)
            }
        }

    }

    override fun clearFilter() {
        viewModel.incompletedTasks.observe(viewLifecycleOwner) {
            val searchTasks = it.filter { taskModel ->
                taskModel.title.contains("", true) || taskModel.description.contains("", true)
            }
            taskAdapter.submitList(searchTasks)
        }
    }

}