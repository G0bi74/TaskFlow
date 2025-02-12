package pl.g0bi74.todolist.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.g0bi74.todolist.MainViewModel
import pl.g0bi74.todolist.Task
import pl.g0bi74.todolist.components.TaskItem
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CompletedTasksScreen(viewModel: MainViewModel = viewModel(), onBack: () -> Unit) {
    val completedTasks by viewModel.completedTasks.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }
    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF211951),
                    Color(0xFF836FFF)
                )
            )
        )


    ) {


        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")


        // Grupowanie zadań według daty zakończenia
        val groupedTasks = completedTasks.groupBy { task ->
            task.deadline.ifEmpty { "Brak daty" }
        }.toSortedMap { date1, date2 ->
            if (date1 == "Brak daty") 1 else if (date2 == "Brak daty") -1
            else LocalDate.parse(date1, formatter).compareTo(LocalDate.parse(date2, formatter))
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Wykonane Zadania", style = MaterialTheme.typography.titleLarge, color = Color(0xFFEBEAFF))
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                groupedTasks.forEach { (date, tasks) ->
                    item {
                        Text("Data: $date", style = MaterialTheme.typography.titleMedium, color = Color(0xFFEBEAFF))
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onComplete = {
                                viewModel.markTaskAsPending(task)
                            },
                            onDelete = {
                                viewModel.deleteTask(task)
                            },
                            onSave = { updatedTask ->
                                viewModel.updateTask(updatedTask)
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }

            Button(onClick = onBack, modifier = Modifier.fillMaxWidth(),colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF211951))) {
                Text("Powrót", color = Color(0xFFEBEAFF))
            }
        }
    }
}