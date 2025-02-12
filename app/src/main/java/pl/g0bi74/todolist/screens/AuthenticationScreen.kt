package pl.g0bi74.todolist.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import pl.g0bi74.todolist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            //R.drawable.logo_list
            Surface(modifier = Modifier.padding(100.dp), shape = RoundedCornerShape(33.dp)) {
                Image(painter = painterResource(id = R.drawable.logo_list), contentDescription = "logo")
            }


            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF9694FF), // Kolor tła pola tekstowego
                    //textColor = Color.White, // Kolor wpisywanego tekstu
                    cursorColor = Color(0xFF211951), // Kolor kursora
                    focusedLabelColor = Color.White, // Kolor etykiety po kliknięciu w pole
                    unfocusedLabelColor = Color.LightGray // Kolor etykiety, gdy pole nie jest aktywne
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                visualTransformation = PasswordVisualTransformation(),
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF9694FF), // Kolor tła pola tekstowego
                    //textColor = Color.White, // Kolor wpisywanego tekstu
                    cursorColor = Color(0xFF211951), // Kolor kursora
                    focusedLabelColor = Color.White, // Kolor etykiety po kliknięciu w pole
                    unfocusedLabelColor = Color.LightGray // Kolor etykiety, gdy pole nie jest aktywne
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                navController.navigate("main") // Przejście do ekranu głównego
                            } else {
                                errorMessage = task.exception?.localizedMessage
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth(),colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF211951))
            ) {
                Text("Log In")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                navController.navigate("main") // Przejście do ekranu głównego
                            } else {
                                errorMessage = task.exception?.localizedMessage
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth(),colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF211951))
            ) {
                Text("Register")
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}