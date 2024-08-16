package com.example.todoapp.addtasks.data.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.addtasks.data.TaskDAO
import com.example.todoapp.addtasks.data.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//VIDEO #116: Implementando Room (Ver min 11:40)
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    //Tenemos que proveer dos cosas, la DB y el DAO

    @Provides
    fun provideTaskDao(todoDatabase: TodoDatabase): TaskDAO {
        return todoDatabase.taskDao()
    }

    @Singleton
    @Provides
    //Necesitamos un contexto, por eso usamos ApplicationContext
    fun provideTodoDatabase(@ApplicationContext appContext: Context): TodoDatabase {
        return Room.databaseBuilder(appContext, TodoDatabase::class.java, "TaskDatabase").build()
    }
}