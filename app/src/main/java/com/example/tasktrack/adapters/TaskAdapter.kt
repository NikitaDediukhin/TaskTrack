package com.example.tasktrack.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.TaskModel
import com.example.tasktrack.R

class TaskAdapter: RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var taskList: List<TaskModel>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setDataTasks(taskData: List<TaskModel>){
        taskList = taskData
        notifyDataSetChanged()
    }

    class TaskViewHolder(item: View): RecyclerView.ViewHolder(item){
        private val tvTitle: TextView = item.findViewById(R.id.tvTaskTitle)
        private val tvDescription: TextView = item.findViewById(R.id.tvTaskDescription)
        private val tvCreationDate: TextView = item.findViewById(R.id.tvTaskCreationDate)
        private val tvDueDate: TextView = item.findViewById(R.id.tvTaskDueDate)
        private val tvStatus: TextView = item.findViewById(R.id.tvTaskStatus)

        fun onBind(task: TaskModel){
            tvTitle.text = task.title
            tvDescription.text = task.description
            tvCreationDate.text = task.creationDate.toString()
            tvDueDate.text = task.dueDate.toString()
            tvStatus.text = if (task.competitionStatus) "выполнено" else "в процессе"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_task_layout, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun getItemCount(): Int = taskList?.size ?: 0

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = taskList?.get(position)
        currentTask?.let {
            holder.onBind(it)
        }
    }
}