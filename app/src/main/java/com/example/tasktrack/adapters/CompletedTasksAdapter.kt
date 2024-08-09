package com.example.tasktrack.adapters

import android.content.Context
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
import com.example.tasktrack.activity.MainActivity
import java.text.SimpleDateFormat
import java.util.Locale

class CompletedTasksAdapter(
    private val context: Context,
    private val onDeleteTask: (TaskModel) -> Unit,
    private val onMarkTask: (TaskModel, Boolean) -> Unit,
    private val onEditTask: (TaskModel) -> Unit,
    private var isGridLayout: Boolean
) : ListAdapter<TaskModel, CompletedTasksAdapter.CompletedTaskViewHolder>(TaskDiffCallback()) {

    class CompletedTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        private val tvDueDate: TextView = itemView.findViewById(R.id.tvTaskDueDate)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvTaskStatus)
        private val btnDelete: ImageView = itemView.findViewById(R.id.ivDeleteTask)
        private val imageViewTaskStatus: ImageView = itemView.findViewById(R.id.btnTaskStatus)
        private val btnTaskEdit: ImageView = itemView.findViewById(R.id.ivEditTask)

        fun onBind(
            context: Context,
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
                // restore task from SnackBar
                (context as? MainActivity)?.restoreDeletedTask(task)
            }

            val isTaskCompleted = task.competitionStatus
            imageViewTaskStatus.setImageResource(
                if (isTaskCompleted) R.drawable.ic_task_complete else R.drawable.ic_task_incomplete
            )
            tvStatus.text = if (isTaskCompleted) "выполнено" else "в процессе"

            imageViewTaskStatus.setOnClickListener {
                val newStatus = !isTaskCompleted
                imageViewTaskStatus.setImageResource(
                    if (newStatus) R.drawable.ic_task_complete else R.drawable.ic_task_incomplete
                )
                tvStatus.text = if (newStatus) "выполнено" else "в процессе"
                onMarkTask(task, newStatus)
            }

            btnTaskEdit.setOnClickListener {
                onEditTask(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedTaskViewHolder {
        val layout = if (isGridLayout) {
            R.layout.item_task_grid_layout
        } else {
            R.layout.item_task_layout
        }
        val itemView = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return CompletedTaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CompletedTaskViewHolder, position: Int) {
        val currentTask = getItem(position)
        holder.onBind(context, currentTask, onDeleteTask, onMarkTask, onEditTask)
    }

    fun setGridLayout(isGrid: Boolean) {
        isGridLayout = isGrid
        notifyDataSetChanged()
    }
}
