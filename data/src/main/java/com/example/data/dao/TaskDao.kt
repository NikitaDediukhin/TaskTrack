package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.entities.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAll(): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getById(id: Int)

    @Insert
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

}