package ru.ildar.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.ildar.designsystem.R
import androidx.compose.material3.Icon

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    centerText: Boolean = false,
    enabled: Boolean = true,
) {
    val centeredTextStyle = if (centerText) {
        MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)
    } else {
        MaterialTheme.typography.bodySmall
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = if (centerText) modifier.fillMaxWidth() else modifier,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_shape_medium)),
        placeholder = {
            Text(
                text = placeholderText,
                style = centeredTextStyle,
                modifier = if (centerText) Modifier.fillMaxWidth() else Modifier
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        textStyle = centeredTextStyle,
        visualTransformation = visualTransformation,
        enabled = enabled
    )
}

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_shape_medium)),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(
                vertical = dimensionResource(R.dimen.padding_small)
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    enabled: Boolean = true,
    centerText: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val centeredTextStyle = if (centerText) {
        MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)
    } else {
        MaterialTheme.typography.bodySmall
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = if (centerText) modifier.fillMaxWidth() else modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_shape_medium)),
        placeholder = {
            Text(
                text = placeholderText,
                style = centeredTextStyle,
                modifier = if (centerText) Modifier.fillMaxWidth() else Modifier
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        textStyle = centeredTextStyle,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        enabled = enabled,
        trailingIcon = {
            IconButton(
                onClick = { passwordVisible = !passwordVisible },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(
                        if (passwordVisible) R.drawable.ic_visibility
                        else R.drawable.ic_visibility_off
                    ),
                    contentDescription = if (passwordVisible) {
                        "Спрятать пароль"
                    } else {
                        "Показать пароль"
                    },
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}
