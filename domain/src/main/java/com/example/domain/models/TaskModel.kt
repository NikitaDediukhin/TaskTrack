package com.example.domain.models

import java.util.Date

data class TaskModel(
    private val id: Int,
    private val title: String,
    private val description: String,
    private val creationDate: Date,
    private val dueDate: Date,
    private val competitionStatus: Boolean
)