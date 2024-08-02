package com.example.todoapp.addtasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.todoapp.addtasks.ui.model.TaskModel

@Composable
fun TasksScreen(tasksViewModel: TasksViewModel) {

    //VIDEO #112: Añadiendo y configurando el diálogo
    val showDialog: Boolean by tasksViewModel.showDialog.observeAsState(false)

    Box(modifier = Modifier.fillMaxSize()) {
        AddTasksDialog(
            show = showDialog,
            onDismiss = { tasksViewModel.onDialogClose() },
            onTaskAdded = { tasksViewModel.onTasksCreated(it) })

        //Recordar que si quiero usar un atributo que solo está disponible dentro del Box, tengo que
        //enviar ese modificador ya PREPARADO al elemento como argumento
        FabDialog(Modifier.align(Alignment.BottomEnd), tasksViewModel)

        //VIDEO #113: Creating the tasks
        TasksList(tasksViewModel)
    }
}

//VIDEO #113: Creating the tasks
@Composable
fun TasksList(tasksViewModel: TasksViewModel) {

    val myTasks: List<TaskModel> = tasksViewModel.tasks

    LazyColumn {
        //Crea un ítem por cada uno de los objetos de esta lista (myTasks)
        /*Recordar que en nuestro recyclerView (LazyColumn), el KEY nos ayuda a optimizar la vista
        del Recycler ya que este le cuesta saber cual item es cada uno. Como sabe bien qué objeto es
         cada uno. Por eso agregamos la propiedad key con el id correspondiente. Al escribir "it.id"
         estamos optimizando el ReclyclerView*/
        items(myTasks, key = { it.id }) { task ->
            ItemTask(taskModel = task, tasksViewModel = tasksViewModel)
        }

    }
}


@Composable
fun ItemTask(taskModel: TaskModel, tasksViewModel: TasksViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .pointerInput(Unit) {
                /* VIDEO #115: Borrando las tareas
                * Vamos a implementar una nueva propiedad, pointerInput, aquí adentro podemos
                * controlar varios tipos de click */
                detectTapGestures(onLongPress = {
                    tasksViewModel.onItemRemove(taskModel)
                })
            },
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = taskModel.task, modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(1f)
            ) //It takes up the entire width
            Checkbox(
                checked = taskModel.selected,
                onCheckedChange = { tasksViewModel.onCheckBoxSelected(taskModel) })
        }
    }
}


@Composable
fun FabDialog(modifier: Modifier, tasksViewModel: TasksViewModel) {
    FloatingActionButton(onClick = {
        tasksViewModel.onShowDialogClick()
    }, modifier = modifier
        .padding(16.dp)) {
        Icon(Icons.Filled.Add, contentDescription = "")
    }
}

@Composable
fun AddTasksDialog(show: Boolean, onDismiss: () -> Unit, onTaskAdded: (String) -> Unit) {
    var myTask by remember { mutableStateOf("") }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)) {
                Text(
                    text = "Add your task",
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = myTask,
                    onValueChange = { myTask = it },
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    onTaskAdded(myTask)
                    myTask = ""
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Add task")
                }
            }
        }
    }
}
