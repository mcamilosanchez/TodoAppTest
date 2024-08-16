package com.example.todoapp.addtasks.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.addtasks.domain.AddTaskUseCase
import com.example.todoapp.addtasks.domain.GetTasksUseCase
import com.example.todoapp.addtasks.domain.RemoveTaskUseCase
import com.example.todoapp.addtasks.domain.UpdateTaskUseCase
import com.example.todoapp.addtasks.ui.TasksUiState.*
import com.example.todoapp.addtasks.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

//VIDEO #119: Preparando el StateFlow. En este video fué donde inyectamos las dependencias
@HiltViewModel
/** Es importante recordar que dentro de los viewModels, se deben inyectar los casos de uso. **/
class TasksViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val removeTaskUseCase: RemoveTaskUseCase,
    getTasksUseCase: GetTasksUseCase
) : ViewModel() {

    /* VIDEO #119: Preparando el StateFlow. (MINUTO 11:35)
    * uiState será nuestro StateFlow, será la encargarda de consumir nuestro caso de uso, lo cual
    * hacemos un map a ACCESS. Es decir, le estamos diciendo: si sale bien, vas a coger el listado y
    * por cada uno de los ítems lo vas a meter (map) a SUCCESS. Si algo sale mal, lo capturamos por
    * medio de .CATCH y el it será el THROWABLE.
    * Luego, STATEIN convierte un flow en un StateFlow
    *       _ viewModelScope, la corrutina donde se encontrará
    *       _ SharingStarted se usa para que cuando la app pase a segundo plano y pasen 5 segundos
    *        y no hemos vuelto a la pantalla, SE PARA EL FLOW.
    *       _ LOADING, será el estado inicial */

    val uiState: StateFlow<TasksUiState> =
        getTasksUseCase()
            .map(::Success)
            .catch { Error(it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(500),
                Loading
            )


    //VIDEO #112: Añadiendo y configurando el diálogo
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    /* VIDEO #113
    Importante: Como necesitamos almacenar los items, podemos usar los LiveData (como hicimos
    anteriormente en showDialog), pero el problema es que no van bien con los listados y más con los
    listados que se van actualizando para temas de RecyclerView. Por lo cual, usaremos un MUTABLE
    STATE LIST. Funciona igual, pero permite que ese listado sea mucho más fácil de manejar. Ya que
    si no hacemos esto, muchas veces no llaman la recomposición del ítem, ya que no es capaz de
    saber que ha sido modificado. Otra solución es FLOW */

    /* VIDEO 120: Conectando StateFlow con Screen
        * Vamos a eliminar _tasks, ya que estamos llamando los ítems desde Flow */

//    private val _tasks = mutableStateListOf<TaskModel>()
//    val tasks: List<TaskModel> = _tasks

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onTasksCreated(task: String) {
        _showDialog.value = false
        /* VIDEO 120: Conectando StateFlow con Screen
        * Vamos a eliminar _tasks, ya que estamos llamando los ítems desde Flow */
        //_tasks.add(TaskModel(task = task))

        /* VIDEO 119: Preparando el StateFlow - MIN 14:30
        * Aquí vamos a añadir una tarea (item) con Flow*/
        viewModelScope.launch {
            addTaskUseCase(TaskModel(task = task))
        }
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {
        //Actualizar Check

        /* VIDEO 120: Conectando StateFlow con Screen
        * Vamos a eliminar _tasks, ya que estamos llamando los ítems desde Flow */

        //Aquí estoy diciendo: búscame el index de este ítem (taskModel) y guardalo en index
//        val index = _tasks.indexOf(taskModel)
//
//        _tasks[index] = _tasks[index].let {
//            it.copy(selected = !it.selected)
//        }

        //VIDEO #122. Actualizando los ítems
        viewModelScope.launch {
            //Lo que hace la función COPY es duplicar el objeto con los valores modificados que le
            // pongamos dentro de su argumento, en este caso, SELECTED. Es decir, le estamos
            // diciendo que solamente modifique SELECTED, ya que la ID tiene que ser la misma y la
            // tarea igual
            updateTaskUseCase(taskModel.copy(selected = !taskModel.selected))
        }

    }

    //VIDEO #115
    fun onItemRemove(taskModel: TaskModel) {
        //Borrar Items

        /* VIDEO 120: Conectando StateFlow con Screen
        * Vamos a eliminar _tasks, ya que estamos llamando los ítems desde Flow */

//        val task = _tasks.find { it.id == taskModel.id }
//        _tasks.remove(task)

        // VIDEO #123. Borrando Items
        // Como sabemos que item borrar? Como nuestra TaskEntity tiene una @PrimaryKey que es el id,
        // el cual es único
        viewModelScope.launch {
            removeTaskUseCase(taskModel)
        }

    }
}