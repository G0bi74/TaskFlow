package pl.g0bi74.todolist.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.checkerframework.common.subtyping.qual.Bottom

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {


            Spacer(modifier = Modifier.height(400.dp))

            Text(
                text = "Just Do it",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

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

            //Spacer, który wypycha dolny pasek do końca ekranu


            // Dolny pasek sterowania
//            Scaffold(
//                bottomBar = {
//
//                }
//            ) {}

        }
        Column(modifier = Modifier.padding(16.dp,0.dp), verticalArrangement = Arrangement.Bottom ) {
            Spacer(modifier = Modifier.height(785.dp)) // telefon emulator
            //Spacer(modifier = Modifier.height(759.dp))   //telefon Mój
            BottomControlBar(
                pendingCount = pendingTasks.size,
                completedCount = completedTasks.size,
                onNavigateToPending = onNavigateToPending,
                onNavigateToCompleted = onNavigateToCompleted,
                onLogout = onLogout
            )
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

    Row {
        Button(onClick = { datePickerDialog.show() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF211951))) {
            Text("Wybierz datę")
        }
        if (selectedDate.isNotEmpty()) {
            Text("     Wybrana data: $selectedDate", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.align(Alignment.CenterVertically))
            /*
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Wybrana data: $selectedDate", style = MaterialTheme.typography.bodyMedium)
            }
             */

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
        color = Color(0xFF211951),
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = onNavigateToCompleted,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF211951))
            ) {
                Text("Done ($completedCount)")
            }
            Button(
                onClick = onLogout,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF211951))
            ) {
                Text("Wyloguj")
            }
            Button(
                onClick = onNavigateToPending,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF211951))
            ) {
                Text("ToDo ($pendingCount)")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF9694FF),
            contentColor = Color.Black
        )
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
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF9694FF), // Kolor tła pola tekstowego
                    //textColor = Color.White, // Kolor wpisywanego tekstu
                    cursorColor = Color(0xFF211951), // Kolor kursora
                    focusedLabelColor = Color.White, // Kolor etykiety po kliknięciu w pole
                    unfocusedLabelColor = Color.LightGray // Kolor etykiety, gdy pole nie jest aktywne
                )
            )
            TextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Opis zadania") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF9694FF), // Kolor tła pola tekstowego
                    //textColor = Color.White, // Kolor wpisywanego tekstu
                    cursorColor = Color(0xFF211951), // Kolor kursora
                    focusedLabelColor = Color.White, // Kolor etykiety po kliknięciu w pole
                    unfocusedLabelColor = Color.LightGray // Kolor etykiety, gdy pole nie jest aktywne
                )
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
                    steps = 4,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF211951),
                        activeTrackColor = Color(0xFF8FD14F),
                        inactiveTrackColor = Color(0xFF70529B)
                    )
                )
            }

            DatePickerExample(onDateSelected = onDeadlineChange)
            Button(
                onClick = onSaveTask,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF211951))
            ) {
                Text("Dodaj zadanie")
            }
        }
    }
}
