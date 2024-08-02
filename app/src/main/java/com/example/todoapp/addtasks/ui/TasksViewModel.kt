package com.example.todoapp.addtasks.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.addtasks.ui.model.TaskModel
import javax.inject.Inject

class TasksViewModel @Inject constructor() : ViewModel() {

    //VIDEO #112: Añadiendo y configurando el diálogo
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    /* VIDEO #113
    Importante: Como necesitamos almacenar los items, podemos usar los LiveData (como hicimos
    anteriormente en showDialog), pero el problema es que no van bien con los listados y más con los
    listados que se van actualizando para temas de RecyclerView. Por lo cual, usaremos un MUTABLE
    STATE LIST. Funciona igual, pero permite que ese listado sea mucho más fácil de manejar. Ya que
    si no hacemos esto, muchas veces no llaman la recomposición del ítem, ya que no es capaz de
    saber que ha sido modificado. Otra solución esFLOW */
    private val _tasks = mutableStateListOf<TaskModel>()
    val tasks: List<TaskModel> = _tasks

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onTasksCreated(task: String) {
        _showDialog.value = false
        _tasks.add(TaskModel(task = task))
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {

        //Aquí estoy diciendo: búscame el index de este ítem (taskModel) y guardalo en index
        val index = _tasks.indexOf(taskModel)

        _tasks[index] = _tasks[index].let {
            it.copy(selected = !it.selected)
        }
    }

    //VIDEO #115
    fun onItemRemove(taskModel: TaskModel) {

        val task = _tasks.find { it.id == taskModel.id }
        _tasks.remove(task)

    }
}