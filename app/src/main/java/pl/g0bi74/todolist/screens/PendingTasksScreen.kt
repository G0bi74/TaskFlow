package pl.g0bi74.todolist.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import pl.g0bi74.todolist.Task
import java.time.format.DateTimeFormatter
import java.time.LocalDate




@Composable
fun PendingTasksScreen(viewModel: MainViewModel = viewModel(), onBack: () -> Unit) {
    val pendingTasks by viewModel.pendingTasks.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Zadania do wykonania", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Grupowanie tasków według deadline
        val groupedTasks = pendingTasks.groupBy { it.deadline }
            .toSortedMap { date1, date2 ->
                val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
                LocalDate.parse(date1, formatter).compareTo(LocalDate.parse(date2, formatter))
            }

        LazyColumn(modifier = Modifier.weight(1f)) {
            groupedTasks.forEach { (deadline, tasks) ->
                item {
                    Text("Deadline: $deadline", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(tasks.sortedByDescending { it.priority }) { task ->
                    TaskItem(
                        task = task,
                        onComplete = {
                            viewModel.markTaskAsCompleted(task)
                        },
                        onDelete = {
                            viewModel.deleteTask(task)
                        },
                        onSave = { updatedTask ->
                            viewModel.updateTask(updatedTask)
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Powrót")
        }
    }
}
