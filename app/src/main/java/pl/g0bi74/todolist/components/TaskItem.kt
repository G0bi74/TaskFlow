
package pl.g0bi74.todolist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun TaskItem(
    taskName: String,
    deadline: String,
    priority: Int,
    onComplete: (() -> Unit)?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = taskName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Deadline: $deadline",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Priority: $priority",
                    style = MaterialTheme.typography.bodyMedium,
                    color = when (priority) {
                        1 -> Color.Red
                        2 -> Color(0xFFFFA500) // Pomarańczowy
                        3 -> Color.Yellow
                        4 -> Color.Green
                        5 -> Color.Blue
                        else -> Color.Gray
                    }
                )
            }

            if (onComplete != null) {
                Button(
                    onClick = onComplete,
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text("Complete")
                }
            }
        }
    }
}



//package pl.g0bi74.todolist.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase

//@Composable
//fun TaskItem(
//    taskId: String,
//    taskName: String,
//    deadline: String,
//    priority: Int,
//    onComplete: (() -> Unit)?
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(MaterialTheme.colorScheme.surface)
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(
//                    text = taskName,
//                    style = MaterialTheme.typography.titleMedium,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = "Deadline: $deadline",
//                    style = MaterialTheme.typography.bodyMedium
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = "Priority: $priority",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = when (priority) {
//                        1 -> Color.Red
//                        2 -> Color(0xFFFFA500) // Pomarańczowy
//                        3 -> Color.Yellow
//                        4 -> Color.Green
//                        5 -> Color.Blue
//                        else -> Color.Gray
//                    }
//                )
//            }
//
//            if (onComplete != null) {
//                Button(
//                    onClick = {
//                        markTaskAsCompleted(taskId, onSuccess = {
//                            onComplete()
//                        }, onFailure = { exception ->
//                            // Możesz obsłużyć błąd tutaj, np. pokazując Toast z wiadomością
//                            println("Błąd: ${exception.message}")
//                        })
//                    },
//                    modifier = Modifier.padding(start = 16.dp)
//                ) {
//                    Text("Complete")
//                }
//            }
//        }
//    }
//}
//fun markTaskAsCompleted(taskId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//    val userId = FirebaseAuth.getInstance().currentUser?.uid
//    val taskRef = FirebaseDatabase.getInstance().getReference("tasks").child(userId ?: "").child(taskId)
//
//    taskRef.child("isCompleted").setValue(true)
//        .addOnSuccessListener {
//            onSuccess()
//        }
//        .addOnFailureListener { exception ->
//            onFailure(exception)
//        }
//}
