package com.example.domain.models

import java.util.Date

data class TaskModel(
    val id: Int,
    val title: String,
    val description: String,
    val creationDate: Date,
    val dueDate: Date,
    var competitionStatus: Boolean
)