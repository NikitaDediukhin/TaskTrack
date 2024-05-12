package com.example.tasktrack.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tasktrack.R
import com.example.tasktrack.fragment.TasksFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, TasksFragment())
            .commit()
    }
}