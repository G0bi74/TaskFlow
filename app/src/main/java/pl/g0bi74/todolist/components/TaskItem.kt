
package pl.g0bi74.todolist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import pl.g0bi74.todolist.Task


@Composable
fun TaskItem(
    task: Task,
    onComplete: () -> Unit,
    onDelete: () -> Unit,
    onSave: (Task) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    // Pola edycyjne
    var editedTitle by remember { mutableStateOf(task.title) }
    var editedDescription by remember { mutableStateOf(task.description) }
    var editedDeadline by remember { mutableStateOf(task.deadline) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { isExpanded = !isExpanded },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF9694FF),
            contentColor = Color.Black
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(task.title, style = MaterialTheme.typography.titleLarge, color = Color(0xFF0B0B36))
                Text("Priorytet: ${task.priority}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF0B0B36))
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (isExpanded) {
                // Pola edycji
                OutlinedTextField(
                    value = editedTitle,
                    onValueChange = { editedTitle = it },
                    label = { Text("Tytuł", color = Color(0xFF0B0B36)) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editedDescription,
                    onValueChange = { editedDescription = it },
                    label = { Text("Opis", color = Color(0xFF0B0B36)) },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                OutlinedTextField(
                    value = editedDeadline,
                    onValueChange = { editedDeadline = it },
                    label = { Text("Deadline", color = Color(0xFF0B0B36)) },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00712D))
                    ) {
                        Text("Zakończ")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onDelete,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF960C05))
                    ) {
                        Text("Usuń")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val updatedTask = task.copy(
                                title = editedTitle,
                                description = editedDescription,
                                deadline = editedDeadline
                            )
                            onSave(updatedTask)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF9CB43))
                    ) {
                        Text("Zapisz")
                    }
                }
            }
        }
    }
}




