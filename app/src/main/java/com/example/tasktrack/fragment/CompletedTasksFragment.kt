package com.example.tasktrack.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.TaskModel
import com.example.tasktrack.activity.MainActivity
import com.example.tasktrack.adapters.CompletedTasksAdapter
import com.example.tasktrack.adapters.IncompletedTasksAdapter
import com.example.tasktrack.databinding.CompletedTasksFragmentBinding
import com.example.tasktrack.utils.DialogManager
import java.util.Date

class CompletedTasksFragment: Fragment(), ViewPagerFragment {

    private lateinit var binding: CompletedTasksFragmentBinding
    private lateinit var taskAdapter: CompletedTasksAdapter
    private lateinit var viewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CompletedTasksFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = (activity as MainActivity).vm

        initRecyclerView()

        viewModel.completedTasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it).apply {
                binding.rvCompletedTasks.post{
                    binding.rvCompletedTasks.smoothScrollToPosition(0)
                }
            }
        }
        viewModel.isGridView.observe(viewLifecycleOwner) {
            taskAdapter.setGridLayout(it)
            setRecyclerViewLayoutManager(it)
        }
    }

    // Toogle Grid
    // recyclerView settings
    private fun initRecyclerView() {

        // RecyclerView & Adapter
        taskAdapter = CompletedTasksAdapter(
            context = requireContext(),
            onDeleteTask = { task -> deleteTask(task) },
            onMarkTask = { task, b -> markTask(task, b) },
            onEditTask = { task -> showEditTaskDialog(task) },
            isGridLayout = viewModel.isGridView.value ?: false
        )

        binding.rvCompletedTasks.adapter = taskAdapter
        setRecyclerViewLayoutManager(viewModel.isGridView.value ?: false)
    }
    private fun setRecyclerViewLayoutManager(isGridLayout: Boolean) {
        if (isGridLayout) {
            binding.rvCompletedTasks.layoutManager = GridLayoutManager(context, 2)
        } else {
            binding.rvCompletedTasks.layoutManager = LinearLayoutManager(context)
        }
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
        viewModel.completedTasks.observe(viewLifecycleOwner) {
            val searchTasks = it.filter { taskModel ->
                taskModel.title.contains(query, true) || taskModel.description.contains(query, true)
            }
            taskAdapter.submitList(searchTasks)
        }
    }

    // Task sorting
    override fun sortTasks(sortBy: String) {
        viewModel.sortCompletedTasks(sortBy).apply {
            binding.rvCompletedTasks.post{
                binding.rvCompletedTasks.smoothScrollToPosition(0)
            }
        }
    }

    override fun clearFilter() {
        viewModel.completedTasks.observe(viewLifecycleOwner) {
            val searchTasks = it.filter { taskModel ->
                taskModel.title.contains("", true) || taskModel.description.contains("", true)
            }
            taskAdapter.submitList(searchTasks)
        }
    }
}