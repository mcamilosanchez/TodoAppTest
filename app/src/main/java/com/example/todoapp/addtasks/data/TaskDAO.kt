package com.example.todoapp.addtasks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//VIDEO #116: Implementando Room
@Dao
interface TaskDAO {

    @Query("SELECT * from TaskEntity")
    fun getTasks(): Flow<List<TaskEntity>>

    //Ahora, debemos añadir los datos
    @Insert
    suspend fun addTask(item: TaskEntity)

    //VIDEO #122. Actualizando los ítems
    @Update
    suspend fun  updateTask(item: TaskEntity)

    //VIDEO #123. Borrando Items
    @Delete
    suspend fun removeTask(item: TaskEntity)

}