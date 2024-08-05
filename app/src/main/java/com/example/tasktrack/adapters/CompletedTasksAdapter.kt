package com.example.tasktrack.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.TaskModel
import com.example.tasktrack.R
import java.text.SimpleDateFormat
import java.util.Locale

class CompletedTasksAdapter(
    private val onDeleteTask: (TaskModel) -> Unit,
    private val onMarkTask: (TaskModel, Boolean) -> Unit,
    private val onEditTask: (TaskModel) -> Unit
) : ListAdapter<TaskModel, CompletedTasksAdapter.CompletedTaskViewHolder>(TaskDiffCallback()) {

    class CompletedTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        // val tvCreationDate: TextView = itemView.findViewById(R.id.tvTaskCreationDate)
        private val tvDueDate: TextView = itemView.findViewById(R.id.tvTaskDueDate)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvTaskStatus)
        private val btnDelete: ImageView = itemView.findViewById(R.id.ivDeleteTask)
        private val btnTaskStatus: SwitchCompat = itemView.findViewById(R.id.btnTaskStatus)
        private val btnTaskEdit: ImageView = itemView.findViewById(R.id.ivEditTask)

        fun onBind(
            task: TaskModel,
            onDeleteTask: (TaskModel) -> Unit,
            onMarkTask: (TaskModel, Boolean) -> Unit,
            onEditTask: (TaskModel) -> Unit
        ) {
            tvTitle.text = task.title
            tvDescription.text = task.description
            //tvCreationDate.text = SimpleDateFormat("dd MMM yyyy, HH", Locale.getDefault()).format(task.creationDate)
            tvDueDate.text = SimpleDateFormat("HH:mm, dd:MMM:yy", Locale.getDefault()).format(task.dueDate)
            tvStatus.text = if (task.competitionStatus) "выполнено" else "в процессе"

            btnDelete.setOnClickListener {
                onDeleteTask(task)
            }

            btnTaskStatus.isChecked = task.competitionStatus
            btnTaskStatus.setOnCheckedChangeListener { _, isChecked ->
                onMarkTask(task, isChecked)
                tvStatus.text = if (isChecked) "выполнено" else "в процессе"
            }

            btnTaskEdit.setOnClickListener {
                onEditTask(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedTaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task_layout, parent, false)
        return CompletedTaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CompletedTaskViewHolder, position: Int) {
        val currentTask = getItem(position)
        holder.onBind(currentTask, onDeleteTask, onMarkTask, onEditTask)
    }
}
