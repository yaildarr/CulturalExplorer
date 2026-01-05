// ru.ildar.auth.ui.screens/SignUpScreen.kt
package ru.ildar.auth_impl.ui.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.ildar.designsystem.CulturalExplorerTheme
import ru.ildar.designsystem.R
import ru.ildar.designsystem.components.CustomButton
import ru.ildar.designsystem.components.CustomTextField
import ru.ildar.designsystem.components.PasswordTextField

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = koinViewModel(),
    onSignUpSuccess: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val viewState = viewModel.viewState.collectAsState()
    val state = viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Обработка состояний
    LaunchedEffect(state) {
        when (val currentState = state) {
            is SignUpState.Success -> {
                onSignUpSuccess()
            }

            is SignUpState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = currentState.message,
                        withDismissAction = true
                    )
                    viewModel.onEvent(SignUpEvent.ResetError)
                }
            }

            else -> Unit
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            .fillMaxSize(),
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Регистрация",
                style = MaterialTheme.typography.displayLarge,
            )

            Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_large)))

            // Поле email
            CustomTextField(
                value = viewState.value.email,
                onValueChange = { viewModel.onEvent(SignUpEvent.EmailChanged(it)) },
                placeholderText = "Введите почту",
                modifier = Modifier.fillMaxWidth(),
            )


            Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small)))

            // Поле пароля
            PasswordTextField(
                value = viewState.value.password,
                onValueChange = { viewModel.onEvent(SignUpEvent.PasswordChanged(it)) },
                placeholderText = "Введите пароль",
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewState.value.isLoading
            )

            // Ошибка
            viewState.value.errorMessage?.let { error ->
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }

        // Кнопка регистрации
        if (viewState.value.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = dimensionResource(R.dimen.padding_large))
            )
        } else {
            CustomButton(
                text = "Продолжить",
                onClick = { viewModel.onEvent(SignUpEvent.SignUpClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewState.value.isSignUpEnabled && !viewState.value.isLoading
            )
        }

        Spacer(modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium)))
    }
}

@Composable
@Preview
private fun Preview() {
    CulturalExplorerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SignUpScreen()
        }
    }
}