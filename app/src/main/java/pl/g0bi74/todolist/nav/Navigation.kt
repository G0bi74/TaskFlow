package pl.g0bi74.todolist.nav

import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.g0bi74.todolist.screens.AuthenticationScreen
import pl.g0bi74.todolist.screens.CompletedTasksScreen
import pl.g0bi74.todolist.screens.MainScreen
import pl.g0bi74.todolist.screens.PendingTasksScreen
import android.Manifest
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext




@Composable
fun Navigation(navController: NavHostController) {




    NavHost(
        navController = navController,
        startDestination = "authentication" // Punkt startowy aplikacji
    ) {
        composable("authentication") {
            AuthenticationScreen(navController) // Ekran logowania i rejestracji
        }
        composable("main") {
            MainScreen(
                onNavigateToCompleted = { navController.navigate("completed") },
                onNavigateToPending = { navController.navigate("pending") },
                onLogout = { navController.popBackStack() }
            )
        }
        composable("completed") {
            CompletedTasksScreen(onBack = { navController.popBackStack() })
        }
        composable("pending") {
            PendingTasksScreen(onBack = { navController.popBackStack() })
        }
    }
}

