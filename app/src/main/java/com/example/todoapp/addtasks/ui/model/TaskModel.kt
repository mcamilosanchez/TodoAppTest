package com.example.todoapp.addtasks.ui.model

//VIDEO #13: Creating the tasks
data class TaskModel(
    val id: Long = System.currentTimeMillis(), //Devuelve el momento exacto cuando haya una acción
    val task: String,
    var selected: Boolean = false
)