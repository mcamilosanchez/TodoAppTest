package com.example.todoapp.addtasks.domain

import com.example.todoapp.addtasks.data.TaskRepository
import com.example.todoapp.addtasks.ui.model.TaskModel
import javax.inject.Inject

//VIDEO #122. Acutalizando los ítems
class UpdateTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskModel: TaskModel) {
        taskRepository.update(taskModel)
    }
}