package com.example.todoapp.addtasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

//VIDEO #116: IMPLEMENTANDO ROOM
@Entity
data class TaskEntity (
    @PrimaryKey
    val id: Int = System.currentTimeMillis().hashCode(),
    val task: String,
    var selected: Boolean = false
)