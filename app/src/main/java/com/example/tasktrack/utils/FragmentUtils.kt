package com.example.tasktrack.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.tasktrack.R
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Date

object DialogManager {
    @SuppressLint("InflateParams")
    fun addTaskDialog(context: Context, listener: Listener){
        val dialog = Dialog(context, R.style.DialogCustomTheme)

        dialog.window?.run {
            setContentView(R.layout.add_task_dialog)
            setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        }
        dialog.setCancelable(false)
        val btnAddTask: Button = dialog.findViewById(R.id.btnAddTask)
        val btnChangeDate: Button = dialog.findViewById(R.id.btnTaskDueDate)
        val ivCancel: ImageView = dialog.findViewById(R.id.ivCancel)
        val etTaskTitle: TextInputEditText = dialog.findViewById(R.id.editTaskTitle)
        val etTaskDescription: TextInputEditText = dialog.findViewById(R.id.editTaskDescription)

        lateinit var dueDate: Date
        val calendar = Calendar.getInstance()

        ivCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnChangeDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(context, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.set(year, monthOfYear, dayOfMonth)

                dueDate = calendar.time
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
        btnAddTask.setOnClickListener {
            listener.onClick(
                title = etTaskTitle.text.toString(),
                description = etTaskDescription.text.toString(),
                dueDate = dueDate
            )
        }

        dialog.show()
    }

    interface Listener{
        fun onClick(title: String, description: String, dueDate: Date)
    }
}