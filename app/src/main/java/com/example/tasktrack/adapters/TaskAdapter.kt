package com.example.tasktrack.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.TaskModel
import com.example.tasktrack.R
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(
    private val onDeleteTask: (TaskModel) -> Unit,
    private val onMarkTask: (TaskModel, Boolean) -> Unit,
    private val onEditTask: (TaskModel) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var taskList: MutableList<TaskModel> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
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
        private val btnTaskStatus: SwitchCompat = item.findViewById(R.id.btnTaskStatus)
        private val btnTaskEdit: ImageView = item.findViewById(R.id.ivEditTask)

        fun onBind(
            task: TaskModel,
            onDeleteTask: (TaskModel) -> Unit,
            onMarkTask: (TaskModel, Boolean) -> Unit,
            onEditTask: (TaskModel) -> Unit
        ) {
            tvTitle.text = task.title
            tvDescription.text = task.description
            tvCreationDate.text = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault()).format(task.creationDate)
            tvDueDate.text = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault()).format(task.dueDate)
            tvStatus.text = if (task.competitionStatus) "выполнено" else "в процессе"

            btnDelete.setOnClickListener {
                onDeleteTask(task)
            }

            btnTaskStatus.isChecked = task.competitionStatus
            btnTaskStatus.setOnCheckedChangeListener { _, isChecked ->
                tvStatus.text = if(isChecked) "выполнено" else "в процессе"
                onMarkTask(task, isChecked)
            }

            btnTaskEdit.setOnClickListener {
                onEditTask(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task_layout, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = taskList[position]
        holder.onBind(currentTask, onDeleteTask, onMarkTask, onEditTask)
    }
}
