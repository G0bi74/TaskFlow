package pl.g0bi74.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Task(
    val taskName: String = "",
    val deadline: String = "",
    val priority: Int = 1,
    val completed: Boolean = false
)

class MainViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Strumienie danych dla zadań
    private val _pendingTasks = MutableStateFlow<List<Task>>(emptyList())
    val pendingTasks: StateFlow<List<Task>> get() = _pendingTasks

    private val _completedTasks = MutableStateFlow<List<Task>>(emptyList())
    val completedTasks: StateFlow<List<Task>> get() = _completedTasks

    fun loadTasks() {
        val currentUser = auth.currentUser ?: return

        db.collection("tasks")
            .whereEqualTo("userId", currentUser.uid) // Pobieraj tylko zadania zalogowanego użytkownika
            .get()
            .addOnSuccessListener { result ->
                val tasks = result.map { doc ->
                    Task(
                        taskName = doc.getString("taskName") ?: "",
                        deadline = doc.getString("deadline") ?: "",
                        priority = (doc.getLong("priority") ?: 1).toInt(),
                        completed = doc.getBoolean("completed") ?: false
                    )
                }
                // Podziel zadania na dwie listy
                _pendingTasks.value = tasks.filter { !it.completed }
                _completedTasks.value = tasks.filter { it.completed }
            }
            .addOnFailureListener { exception ->
                // Obsługa błędów
                println("Error loading tasks: ${exception.message}")
            }
    }
    fun markTaskAsCompleted(taskId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val taskRef = FirebaseDatabase.getInstance().getReference("tasks").child(userId ?: "").child(taskId)

        taskRef.child("isCompleted").setValue(true)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}
