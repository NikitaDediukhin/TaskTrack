package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.converters.DateConverter
import com.example.data.dao.TaskDao
import com.example.data.entities.Task

@Database(entities = [Task::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun taskDAO(): TaskDao

    companion object {
        private const val DATABASE_NAME = "Tasks.db"
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance?: Room.databaseBuilder(
                context = context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).allowMainThreadQueries()
             .fallbackToDestructiveMigration()
             .build().also { instance = it }
        }
    }
}