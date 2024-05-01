package com.example.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    val creationDate: Long,
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    val dueDate: Long,
    val competitionStatus: Boolean

)