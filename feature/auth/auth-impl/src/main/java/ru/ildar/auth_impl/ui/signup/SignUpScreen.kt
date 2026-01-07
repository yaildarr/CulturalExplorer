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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.ildar.designsystem.CulturalExplorerTheme
import ru.ildar.designsystem.R
import ru.ildar.designsystem.components.CustomButton
import ru.ildar.designsystem.components.CustomTextField
import ru.ildar.designsystem.components.PasswordTextField
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = koinViewModel(),
    onSignUpSuccess: () -> Unit = {},
    onNavigateSignIn: () -> Unit = {}
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect {
        when(it){
            SignUpSideEffect.ShowSuccess -> {onSignUpSuccess()}
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            .fillMaxSize(),
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = stringResource(ru.ildar.auth_impl.R.string.register_label),
                style = MaterialTheme.typography.displayLarge,
            )

            Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_large)))

            CustomTextField(
                value = state.email,
                onValueChange = { viewModel.onAction(SignUpAction.EmailChanged(it)) },
                placeholderText = stringResource(ru.ildar.auth_impl.R.string.enter_email),
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            )


            Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small)))

            PasswordTextField(
                value = state.password,
                onValueChange = { viewModel.onAction(SignUpAction.PasswordChanged(it)) },
                placeholderText = stringResource(ru.ildar.auth_impl.R.string.enter_password),
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            )

            Spacer(modifier.padding(dimensionResource(id = R.dimen.padding_small)))

            Text(
                text = stringResource(ru.ildar.auth_impl.R.string.or),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier.padding(dimensionResource(id = R.dimen.padding_small)))

            CustomButton(
                text = stringResource(ru.ildar.auth_impl.R.string.sign_in),
                onClick = {onNavigateSignIn()},
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_extra_large))
                    .fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
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
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = dimensionResource(R.dimen.padding_large))
            )
        } else {
            CustomButton(
                text = stringResource(ru.ildar.auth_impl.R.string.Continue),
                onClick = { viewModel.onAction(SignUpAction.SignUpClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isSignUpEnabled && !state.isLoading
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