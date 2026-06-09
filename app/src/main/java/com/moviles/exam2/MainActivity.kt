package com.moviles.exam2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moviles.exam2.data.repository.MockBugRepository
import com.moviles.exam2.ui.screen.BugDetailScreen
import com.moviles.exam2.ui.screen.BugListScreen
import com.moviles.exam2.ui.screen.CreateBugScreen
import com.moviles.exam2.ui.screen.LoginScreen
import com.moviles.exam2.ui.viewmodel.BugListViewModel
import com.moviles.exam2.ui.theme.Exam2Theme
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // En un escenario real usaríamos Inyección de Dependencias (Dagger/Hilt/Koin)
        val repository = MockBugRepository()
        val viewModel = BugListViewModel(repository)

        enableEdgeToEdge()
        setContent {
            Exam2Theme {
                AppNavigation(viewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: BugListViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    // Navegamos al listado y eliminamos el Login del backstack
                    navController.navigate("bug_list") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("bug_list") {
            BugListScreen(
                viewModel = viewModel,
                onBugClick = { bugId ->
                    navController.navigate("bug_detail/$bugId")
                },
                onCreateClick = {
                    navController.navigate("create_bug")
                }
            )
        }
        composable("create_bug") {
            CreateBugScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "bug_detail/{bugId}",
            arguments = listOf(navArgument("bugId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bugId = backStackEntry.arguments?.getString("bugId") ?: ""
            BugDetailScreen(
                bugId = bugId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
