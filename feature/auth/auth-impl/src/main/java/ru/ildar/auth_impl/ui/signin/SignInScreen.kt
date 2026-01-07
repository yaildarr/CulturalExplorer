package ru.ildar.auth_impl.ui.signin

import android.R.attr.shadowColor
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.ildar.auth_impl.ui.signup.SignUpScreen
import ru.ildar.designsystem.CulturalExplorerTheme
import ru.ildar.designsystem.R
import ru.ildar.designsystem.components.CustomButton
import ru.ildar.designsystem.components.CustomTextField
import ru.ildar.designsystem.components.PasswordTextField

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = koinViewModel(),
    onSignInSuccess: () -> Unit = {},
    onNavigateSignUp: () -> Unit = {}
) {
    val state = viewModel.collectAsState().value



    viewModel.collectSideEffect {
        when(it){
            SignInSideEffect.ShowSuccess -> {onSignInSuccess()}
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            text = stringResource(ru.ildar.auth_impl.R.string.sign_in_label),
            style = MaterialTheme.typography.displayLarge,
        )
        Spacer(Modifier.padding(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp,
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceBright
            )
        ){
            Column(Modifier.padding(16.dp)) {

                Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small)))

                CustomTextField(
                    value = state.email,
                    onValueChange = { viewModel.onAction(SignInAction.EmailChanged(it)) },
                    placeholderText = stringResource(ru.ildar.auth_impl.R.string.enter_email),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                )


                Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small)))

                PasswordTextField(
                    value = state.password,
                    onValueChange = { viewModel.onAction(SignInAction.PasswordChanged(it)) },
                    placeholderText = stringResource(ru.ildar.auth_impl.R.string.enter_password),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                )

                state.errorMessage?.let { error ->
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier.padding(dimensionResource(id = R.dimen.padding_medium)))


                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = dimensionResource(R.dimen.padding_large))
                    )
                } else {
                    CustomButton(
                        text = stringResource(ru.ildar.auth_impl.R.string.Continue),
                        onClick = { viewModel.onAction(SignInAction.SignInClicked) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.isSignInEnabled && !state.isLoading
                    )
                }

                Spacer(modifier.padding(dimensionResource(id = R.dimen.padding_small)))

                CustomButton(
                    text = stringResource(ru.ildar.auth_impl.R.string.create_acc),
                    onClick = {onNavigateSignUp()},
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_extra_large))
                        .fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )


                Spacer(modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)))
            }
        }

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