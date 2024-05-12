package com.example.tasktrack.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tasktrack.R
import com.example.tasktrack.databinding.TasksFragmentBinding
import com.example.tasktrack.utils.DialogManager
import java.util.Date

class TasksFragment: Fragment() {

    private lateinit var binding: TasksFragmentBinding

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

        val btnAddTask: View = binding.root.findViewById(R.id.btnAddTask)

        btnAddTask.setOnClickListener{
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        context?.let { context ->
            DialogManager.addTaskDialog(context, object : DialogManager.Listener {
                override fun onClick(title: String, description: String, changeDate: Date, dueDate: Date) {
                }
            })
        }
    }

}