package pl.g0bi74.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Task(
    val id: String? = null, // Identyfikator dokumentu Firestore
    val title: String = "",
    val deadline: String = "",
    val description: String = "",
    val priority: Int = 1,
    val completed: Boolean = false,
    val userId: String = ""
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
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("tasks")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Błąd pobierania zadań: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val pending = mutableListOf<Task>()
                    val completed = mutableListOf<Task>()

                    for (doc in snapshot.documents) {
                        val task = doc.toObject(Task::class.java)?.copy(id = doc.id)
                        if (task != null) {
                            if (task.completed) {
                                completed.add(task)
                            } else {
                                pending.add(task)
                            }
                        }
                    }

                    _pendingTasks.value = pending
                    _completedTasks.value = completed
                }
            }
    }



    fun markTaskAsCompleted(task: Task) {
        val taskId = task.id ?: return

        db.collection("tasks").document(taskId)
            .update("completed", true)
            .addOnSuccessListener {
                println("Zadanie oznaczone jako wykonane")
                loadTasks()
            }
            .addOnFailureListener { e ->
                println("Błąd przy aktualizacji zadania: $e")
            }
    }


    fun deleteTask(task: Task) {
        val taskId = task.id ?: return

        db.collection("tasks").document(taskId)
            .delete()
            .addOnSuccessListener {
                println("Zadanie usunięte")
                loadTasks()
            }
            .addOnFailureListener { e ->
                println("Błąd usuwania zadania: $e")
            }
    }


    fun updateTask(task: Task) {
        val taskId = task.id ?: return

        db.collection("tasks").document(taskId)
            .set(task)
            .addOnSuccessListener {
                println("Zadanie zaktualizowane")
                loadTasks()
            }
            .addOnFailureListener { e ->
                println("Błąd aktualizacji zadania: $e")
            }
    }



}
