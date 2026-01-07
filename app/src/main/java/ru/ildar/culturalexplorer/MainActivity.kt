package ru.ildar.culturalexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import ru.ildar.auth_impl.ui.signup.SignUpScreen
import ru.ildar.culturalexplorer.navigation.AppNavigation
import ru.ildar.designsystem.CulturalExplorerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CulturalExplorerTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                    )
                { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding)
                    ){
                        AppNavigation(navController = navController)
                    }
                }
            }
        }
    }
}
