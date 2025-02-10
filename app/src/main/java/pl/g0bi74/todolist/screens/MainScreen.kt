package pl.g0bi74.todolist.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

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
        TaskFormCard(
            title = title,
            description = description,
            priority = priority,
            deadline = deadline,
            onTitleChange = { title = it },
            onDescriptionChange = { description = it },
            onPriorityChange = { priority = it },
            onDeadlineChange = { deadline = it },
            onSaveTask = {
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
                        onFailure = { message = "Błąd zapisu: ${it.message}" }
                    )
                } else {
                    message = "Wszystkie pola są wymagane!"
                }
            }
        )

    }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {


            Spacer(modifier = Modifier.height(400.dp))

            Text(text = "Just Do it", style = MaterialTheme.typography.displayLarge, modifier = Modifier.align(Alignment.CenterHorizontally))

            Spacer(modifier = Modifier.height(20.dp))
            // Wyświetlanie zadania o najwyższym priorytecie
            pendingTasks.maxByOrNull { it.priority }?.let { highestPriorityTask ->
                TaskItem(
                    task = highestPriorityTask,
                    onComplete = { viewModel.markTaskAsCompleted(highestPriorityTask) },
                    onDelete = { viewModel.deleteTask(highestPriorityTask) },
                    onSave = { updatedTask -> viewModel.updateTask(updatedTask) }
                )
            } ?: Text("Brak zadań do wykonania", style = MaterialTheme.typography.bodyMedium)

            // Spacer, który wypycha dolny pasek do końca ekranu
            Spacer(modifier = Modifier.weight(1f))

            // Dolny pasek sterowania
            Scaffold(
                bottomBar = {
                    BottomControlBar(
                        pendingCount = pendingTasks.size,
                        completedCount = completedTasks.size,
                        onNavigateToPending = onNavigateToPending,
                        onNavigateToCompleted = onNavigateToCompleted,
                        onLogout = onLogout
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {}
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

    var selectedDate by remember { mutableStateOf("") }

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            onDateSelected(selectedDate)
        },
        year,
        month,
        day
    )

    Column {
        Button(onClick = { datePickerDialog.show() }) {
            Text("Wybierz datę")
        }
        if (selectedDate.isNotEmpty()) {
            Text("Wybrana data: $selectedDate", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun BottomControlBar(
    pendingCount: Int,
    completedCount: Int,
    onNavigateToPending: () -> Unit,
    onNavigateToCompleted: () -> Unit,
    onLogout: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onNavigateToCompleted,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Done ($completedCount)")
            }
            Button(
                onClick = onLogout,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Wyloguj")
            }
            Button(
                onClick = onNavigateToPending,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("ToDo ($pendingCount)")
            }
        }
    }
}

@Composable
fun TaskFormCard(
    title: String,
    description: String,
    priority: Int,
    deadline: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (Int) -> Unit,
    onDeadlineChange: (String) -> Unit,
    onSaveTask: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Dodaj nowe zadanie", style = MaterialTheme.typography.titleMedium)
            TextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Tytuł zadania") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Opis zadania") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Priorytet: $priority")
                Slider(
                    value = priority.toFloat(),
                    onValueChange = { onPriorityChange(it.toInt()) },
                    valueRange = 1f..5f,
                    steps = 4
                )
            }

            DatePickerExample(onDateSelected = onDeadlineChange)
            Button(
                onClick = onSaveTask,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dodaj zadanie")
            }
        }
    }
}
