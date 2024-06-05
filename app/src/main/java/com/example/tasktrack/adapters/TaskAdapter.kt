package com.example.tasktrack.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.TaskModel
import com.example.tasktrack.R
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(
private val onDeleteTask: (TaskModel) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var taskList: MutableList<TaskModel> = mutableListOf()

    fun submitList(list: List<TaskModel>) {
        taskList.clear()
        taskList.addAll(list)
        notifyDataSetChanged()
    }

    class TaskViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val tvTitle: TextView = item.findViewById(R.id.tvTaskTitle)
        private val tvDescription: TextView = item.findViewById(R.id.tvTaskDescription)
        private val tvCreationDate: TextView = item.findViewById(R.id.tvTaskCreationDate)
        private val tvDueDate: TextView = item.findViewById(R.id.tvTaskDueDate)
        private val tvStatus: TextView = item.findViewById(R.id.tvTaskStatus)
        private val btnDelete: ImageView = item.findViewById(R.id.ivDeleteTask)

        fun onBind(task: TaskModel, onDeleteTask: (TaskModel) -> Unit) {
            tvTitle.text = task.title
            tvDescription.text = task.description
            tvCreationDate.text = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault()).format(task.creationDate)
            tvDueDate.text = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault()).format(task.dueDate)
            tvStatus.text = if (task.competitionStatus) "выполнено" else "в процессе"

            btnDelete.setOnClickListener {
                onDeleteTask(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_task_layout, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = taskList[position]
        holder.onBind(currentTask, onDeleteTask)
    }
}
