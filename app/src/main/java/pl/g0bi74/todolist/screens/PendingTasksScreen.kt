package pl.g0bi74.todolist.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.g0bi74.todolist.MainViewModel
import pl.g0bi74.todolist.components.TaskItem
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PendingTasksScreen(viewModel: MainViewModel = viewModel(), onBack: () -> Unit) {
    val pendingTasks by viewModel.pendingTasks.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Zadania do wykonania", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        pendingTasks.forEach { task ->
            TaskItem(
                task = task,
                onComplete = { viewModel.markTaskAsCompleted(task) },
                onDelete = { viewModel.deleteTask(task) },
                onSave = { updatedTask -> viewModel.updateTask(updatedTask) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Powrót")
        }
    }
}

