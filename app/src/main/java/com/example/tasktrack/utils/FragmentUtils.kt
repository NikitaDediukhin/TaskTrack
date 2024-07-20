package com.example.tasktrack.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.domain.models.TaskModel
import com.example.tasktrack.R
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Date

object DialogManager {
    @SuppressLint("InflateParams")
    fun addTaskDialog(context: Context, createTaskListener: CreateTaskListener) {
        val dialog = Dialog(context, R.style.DialogCustomTheme)

        dialog.window?.run {
            setContentView(R.layout.add_task_dialog)
            setLayout(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        dialog.setCancelable(false)
        val btnAddTask: Button = dialog.findViewById(R.id.btnAddTask)
        val btnChangeDate: Button = dialog.findViewById(R.id.btnTaskDueDate)
        val ivCancel: ImageView = dialog.findViewById(R.id.ivCancel)
        val etTaskTitle: TextInputEditText = dialog.findViewById(R.id.editTaskTitle)
        val etTaskDescription: TextInputEditText = dialog.findViewById(R.id.editTaskDescription)

        var dueDate: Date? = null
        val calendar = Calendar.getInstance()
        val currentDate = Calendar.getInstance()
        var isDateSelected = false

        ivCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnChangeDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    dueDate = calendar.time
                    isDateSelected = true
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.datePicker.minDate = currentDate.timeInMillis

            datePickerDialog.show()
        }
        btnAddTask.setOnClickListener {
            val title = etTaskTitle.text.toString().trim()
            val description = etTaskDescription.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(context, "Пожалуйста, укажите название задачи", Toast.LENGTH_SHORT).show()
            } else if (!isDateSelected) {
                Toast.makeText(context, "Пожалуйста, выберите дату", Toast.LENGTH_SHORT).show()
            } else {
                dueDate?.let {
                    createTaskListener.onClick(title, description, it)
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    fun editTaskDialog(context: Context, task: TaskModel, editTaskListener: EditTaskListener) {
        val dialog = Dialog(context, R.style.DialogCustomTheme)

        dialog.window?.run {
            setContentView(R.layout.edit_task_dialog)
            setLayout(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        dialog.setCancelable(false)
        val btnEditTask: Button = dialog.findViewById(R.id.btnEditTask)
        val btnEditDueDate: Button = dialog.findViewById(R.id.btnEditTaskDueDate)
        val ivEditCancel: ImageView = dialog.findViewById(R.id.ivEditCancel)
        val etEditTaskTitle: TextInputEditText = dialog.findViewById(R.id.editEditTaskTitle)
        val etEditTaskDescription: TextInputEditText =
            dialog.findViewById(R.id.editEditTaskDescription)

        var dueDate: Date? = null
        val currentDate = Calendar.getInstance()
        var isDateSelected = false
        val calendar = Calendar.getInstance().apply {
            time = task.dueDate
        }
        etEditTaskTitle.setText(task.title)
        etEditTaskDescription.setText(task.description)

        ivEditCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnEditDueDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    dueDate = calendar.time
                    isDateSelected = true
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.datePicker.minDate = currentDate.timeInMillis

            datePickerDialog.show()
        }

        btnEditTask.setOnClickListener {
            val title = etEditTaskTitle.text.toString().trim()
            val description = etEditTaskDescription.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(context, "Пожалуйста, укажите название задачи", Toast.LENGTH_SHORT).show()
            } else if (!isDateSelected) {
                Toast.makeText(context, "Пожалуйста, выберите дату", Toast.LENGTH_SHORT).show()
            } else {
                dueDate?.let {
                    editTaskListener.onClick(title, description, it)
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    interface CreateTaskListener {
        fun onClick(title: String, description: String, dueDate: Date)
    }

    interface EditTaskListener {
        fun onClick(title: String, description: String, dueDate: Date)
    }
}