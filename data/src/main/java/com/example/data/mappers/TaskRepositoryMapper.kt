package com.example.data.mappers

import com.example.data.entities.Task
import com.example.domain.models.TaskModel
import java.util.Date

class TaskRepositoryMapper {

    fun toTaskModel(task: Task): TaskModel {
        return TaskModel(
            title = task.title,
            description = task.description,
            creationDate = Date(task.creationDate),
            dueDate = Date(task.dueDate),
            competitionStatus = task.competitionStatus
        )
    }

    fun toTaskEntity(taskModel: TaskModel): Task {
        return Task(
            id = 0,
            title = taskModel.title,
            description = taskModel.description,
            creationDate = taskModel.creationDate.time,
            dueDate = taskModel.dueDate.time,
            competitionStatus = taskModel.competitionStatus
        )
    }
}