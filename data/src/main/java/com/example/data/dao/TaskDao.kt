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
    suspend fun getAll(): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: Int): Task

    @Insert
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task): Int

    @Query("UPDATE tasks SET competitionStatus = :status WHERE id = :id")
    suspend fun markTaskById(id: Int, status: Boolean): Int

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun delete(id: Int): Int

}