package com.example.domain.models

import java.time.LocalDate

data class Task(
    private val id: Int,
    private val title: String,
    private val description: String,
    private val creationDate: LocalDate,
    private val dueDate: LocalDate,
    private val competitionStatus: Boolean
)