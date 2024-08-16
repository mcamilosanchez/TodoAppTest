package com.example.todoapp.addtasks.data

import com.example.todoapp.addtasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

//VIDEO #117: Preparando el Repositorio
@Singleton
class TaskRepository @Inject constructor(private val taskDAO: TaskDAO) {

    /*Para realizar la lectura, usaremos Flow (es una comunicación parecida a LiveData, está
    siempre escuchando)*/

    /** IMPORTANTE: Si observamos a partir del minuto 4:50, habrá un problema a nivel de
     * arquitectura con la variable "tasks" ya que se encuentra en el nivel de DATA (ya que
     * TaskEntity se encuentra en DATA y las capas DOMAIN y UI no deberían saber que hay en DATA,
     * es decir, no pueden estar en comunicacción) y la capa que llamará a "tasks" será el DOMINIO
     * y esto no puede suceder. Entonces, NO LES VOY A DAR una lista de TaskEntity, si no una lista
     * de TaskModel que es lo que necesita DOMINIO.
     * Entonces, para solucionar el problema anterior, tenemos que realizar un "mapper": recibimos
     * los datos y se devuelven transformados para cada una de las capas. Con esto, se mantiene el
     * mismo modelo de datos en la capa DOMINIO y UI, por eso usamos ".map"**/


    val tasks: Flow<List<TaskModel>> =
        taskDAO.getTasks().map { items -> items.map { TaskModel(it.id, it.task, it.selected) } }


    //Para añadir:
    suspend fun add(taskModel: TaskModel) {
        taskDAO.addTask(taskModel.toData())
    }

    //VIDEO #122. Acutalizando los ítems
    suspend fun update(taskModel: TaskModel) {
        taskDAO.updateTask(taskModel.toData())
    }

    //VIDEO #123. Borrando Items
    suspend fun remove(taskModel: TaskModel) {
        taskDAO.removeTask(taskModel.toData())
    }

}

//VIDEO #123. Borrando Items: Aquí abajo implementamos una función de extensión, queda pendiente
// estudiarlas.

fun TaskModel.toData(): TaskEntity {
    return TaskEntity(this.id, this.task, this.selected)
}