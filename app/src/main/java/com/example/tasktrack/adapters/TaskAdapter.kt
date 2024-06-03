package com.example.tasktrack.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.TaskModel
import com.example.tasktrack.R
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter : ListAdapter<TaskModel, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    class TaskViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val tvTitle: TextView = item.findViewById(R.id.tvTaskTitle)
        private val tvDescription: TextView = item.findViewById(R.id.tvTaskDescription)
        private val tvCreationDate: TextView = item.findViewById(R.id.tvTaskCreationDate)
        private val tvDueDate: TextView = item.findViewById(R.id.tvTaskDueDate)
        private val tvStatus: TextView = item.findViewById(R.id.tvTaskStatus)

        fun onBind(task: TaskModel) {
            tvTitle.text = task.title
            tvDescription.text = task.description
            tvCreationDate.text = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault()).format(task.creationDate)
            tvDueDate.text = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault()).format(task.dueDate)
            tvStatus.text = if (task.competitionStatus) "выполнено" else "в процессе"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_task_layout, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = getItem(position)
        holder.onBind(currentTask)
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<TaskModel>() {

        override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
            return oldItem == newItem
        }
    }
}
