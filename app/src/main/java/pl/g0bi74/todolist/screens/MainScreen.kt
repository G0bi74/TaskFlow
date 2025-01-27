package pl.g0bi74.todolist.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pl.g0bi74.todolist.MainViewModel
import pl.g0bi74.todolist.components.TaskItem
import java.util.Calendar
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    onNavigateToCompleted: () -> Unit,
    onNavigateToPending: () -> Unit,
    onLogout: () -> Unit
) {
    val pendingTasks by viewModel.pendingTasks.collectAsState()
    val completedTasks by viewModel.completedTasks.collectAsState()

    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(1) }
    var message by remember { mutableStateOf("") }

    // Pobierz zadania przy pierwszym uruchomieniu
    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Dodaj nowe zadanie", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Tytuł zadania") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Opis zadania") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Priorytet: $priority")
        Slider(
            value = priority.toFloat(),
            onValueChange = { priority = it.toInt() },
            valueRange = 1f..5f,
            steps = 4
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            // Wybór daty za pomocą DatePicker
            DatePickerExample(onDateSelected = { selectedDate ->
                deadline = selectedDate
            })
            Button(onClick = {
                if (title.isNotEmpty() && deadline.isNotEmpty()) {
                    saveTaskToFirebase(
                        db = db,
                        userId = userId,
                        title = title,
                        description = description,
                        deadline = deadline,
                        priority = priority,
                        onSuccess = {
                            message = "Zadanie zapisane!"
                            title = ""
                            description = ""
                            deadline = ""
                            priority = 1
                        },
                        onFailure = {
                            message = "Błąd zapisu: ${it.message}"
                        }
                    )
                } else {
                    message = "Wszystkie pola są wymagane!"
                }
            }) {
                Text("Dodaj zadanie")
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(text = "ToDo List", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            // Wyświetlanie zadania o najwyższym priorytecie
            pendingTasks.maxByOrNull { it.priority }?.let { highestPriorityTask ->
                TaskItem(
                    taskName = highestPriorityTask.taskName,
                    deadline = highestPriorityTask.deadline,
                    priority = highestPriorityTask.priority,
                    onComplete = {
                        // Funkcja obsługi ukończenia zadania
                    }
                )
            } ?: Text("Brak zadań do wykonania", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Nawigacja do innych ekranów
            Button(onClick = onNavigateToPending) {
                Text("Zadania do wykonania (${pendingTasks.size})")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onNavigateToCompleted) {
                Text("Wykonane zadania (${completedTasks.size})")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onLogout) {
                Text("Wyloguj")
            }
        }
    }
}
fun saveTaskToFirebase(
    db: FirebaseFirestore,
    userId: String,
    title: String,
    description: String,
    deadline: String,
    priority: Int,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
        Log.d("FirebaseAuth", "User is logged in: ${currentUser.uid}")
    } else {
        Log.e("FirebaseAuth", "No user is logged in")
    }

    val task = hashMapOf(
        "userId" to userId,
        "title" to title,
        "description" to description,
        "deadline" to deadline,
        "priority" to priority,
        "completed" to false
    )

    db.collection("tasks").add(task)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { exception -> onFailure(exception) }
}

@Composable
fun DatePickerExample(onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Zmienna na wybraną datę
    var selectedDate by remember { mutableStateOf("") }

    // Pokazuje DatePickerDialog
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            onDateSelected(selectedDate) // Przekazuje datę do wywołania zwrotnego
        },
        year,
        month,
        day
    )

    // UI przycisku i wyświetlanej daty
    Column {
        Button(onClick = { datePickerDialog.show() }) {
            Text("Wybierz datę")
        }
        if (selectedDate.isNotEmpty()) {
            Text("Wybrana data: $selectedDate", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


