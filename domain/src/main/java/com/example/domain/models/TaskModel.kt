package com.example.domain.models

import java.util.Date

data class TaskModel(
    val id: Int,
    var title: String,
    var description: String,
    val creationDate: Date,
    var dueDate: Date,
    var competitionStatus: Boolean
)