package ru.ildar.culturalexplorer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import ru.ildar.auth_impl.ui.signup.SignUpScreen
import ru.ildar.auth_impl.ui.signup.SignUpViewModel
import ru.ildar.culturalexplorer.AppViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel = koinViewModel()
){
    val isAuthenticated = appViewModel.isAuthenticated.collectAsState()
    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated.value) Routes.MAIN_SCREEN else Routes.SIGN_UP_SCREEN
    ){
        composable(route = Routes.SIGN_UP_SCREEN){
            SignUpScreen()
        }
    }
}