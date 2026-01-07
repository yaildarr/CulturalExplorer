package ru.ildar.culturalexplorer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import ru.ildar.auth_impl.ui.signin.SignInScreen
import ru.ildar.auth_impl.ui.signup.SignUpScreen
import ru.ildar.book_impl.booklist.presentation.BookListScreen
import ru.ildar.culturalexplorer.AppViewModel
import ru.ildar.culturalexplorer.MainScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel = koinViewModel()
){
    val isAuthenticated = appViewModel.isAuthenticated.collectAsState()
    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated.value) Routes.MAIN_SCREEN else Routes.SIGN_IN_SCREEN
    ){
        composable(route = Routes.SIGN_UP_SCREEN){
            SignUpScreen(
                onSignUpSuccess = {navController.navigate(Routes.MAIN_SCREEN){
                    popUpTo(Routes.SIGN_UP_SCREEN) {
                        inclusive = true
                    }
                }},
                onNavigateSignIn = {navController.navigate(Routes.SIGN_IN_SCREEN){
                    popUpTo(Routes.SIGN_UP_SCREEN) {
                        inclusive = true
                    }
                }
                }
            )
        }

        composable(route = Routes.SIGN_IN_SCREEN){
            SignInScreen(
                onSignInSuccess = {navController.navigate(Routes.MAIN_SCREEN){
                    popUpTo(Routes.SIGN_IN_SCREEN) {
                        inclusive = true
                    }
                } },
                onNavigateSignUp = {navController.navigate(Routes.SIGN_UP_SCREEN){
                    popUpTo(Routes.SIGN_IN_SCREEN) {
                        inclusive = true
                    }
                } }
            )
        }

        composable(route = Routes.MAIN_SCREEN) {
            MainScreen({},{navController.navigate(Routes.BOOK_SCREEN)},{},{})
        }

        composable(route = Routes.BOOK_SCREEN) {
            BookListScreen({})
        }
    }
}