package com.example.domain.models

import java.util.Date

data class TaskModel(
    val title: String,
    val description: String,
    val creationDate: Date,
    val dueDate: Date,
    val competitionStatus: Boolean
)