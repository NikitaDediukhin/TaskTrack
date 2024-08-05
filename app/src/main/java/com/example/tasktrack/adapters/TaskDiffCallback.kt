package com.example.tasktrack.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.models.TaskModel

class TaskDiffCallback : DiffUtil.ItemCallback<TaskModel>() {
    override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
        return oldItem == newItem
    }
}