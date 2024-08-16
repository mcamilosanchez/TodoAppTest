package com.example.todoapp.addtasks.data

import androidx.room.Database
import androidx.room.RoomDatabase

//VIDEO #116: Implementando Room
@Database(entities = [TaskEntity::class], version = 1)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDAO

}