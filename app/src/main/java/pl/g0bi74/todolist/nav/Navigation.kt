package pl.g0bi74.todolist.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.g0bi74.todolist.screens.AuthenticationScreen
import pl.g0bi74.todolist.screens.CompletedTasksScreen
import pl.g0bi74.todolist.screens.MainScreen
import pl.g0bi74.todolist.screens.PendingTasksScreen

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

