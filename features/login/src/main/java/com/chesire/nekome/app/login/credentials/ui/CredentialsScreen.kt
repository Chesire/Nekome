@file:OptIn(ExperimentalComposeUiApi::class)

package com.chesire.nekome.app.login.credentials.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chesire.nekome.app.login.R

@Composable
fun CredentialsScreen(
    state: State<ViewState>,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onForgotPasswordPressed: () -> Unit,
    onLoginPressed: () -> Unit,
    onSignupPressed: () -> Unit
) {
    val value = state.value

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderImage()
        UsernameInput(value.username, onUsernameChanged)
        PasswordInput(value.password, onPasswordChanged)
        ForgotPasswordButton(onForgotPasswordPressed)
        Spacer(modifier = Modifier.weight(1f))
        LoginButton(onLoginPressed)
        SignupButton(onSignupPressed)
    }
}

@Composable
private fun HeaderImage() {
    Image(
        painter = painterResource(id = R.drawable.blackcat),
        contentDescription = null
    )
}

@Composable
private fun UsernameInput(username: String, onUsernameChanged: (String) -> Unit) {
    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChanged,
        label = { Text(text = stringResource(id = R.string.login_username)) }
    )
}

@Composable
private fun PasswordInput(password: String, onPasswordChanged: (String) -> Unit) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChanged,
        label = { Text(text = stringResource(id = R.string.login_password)) }
    )
}

@Composable
private fun ForgotPasswordButton(onForgotPasswordPressed: () -> Unit) {
    TextButton(onClick = onForgotPasswordPressed) {
        Text(text = stringResource(id = R.string.login_forgot_password))
    }
}

@Composable
private fun LoginButton(onLoginPressed: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Button(
        onClick = {
            onLoginPressed()
            keyboardController?.hide()
        },
    ) {
        Text(text = stringResource(id = R.string.login_login))
    }
}

@Composable
private fun SignupButton(onSignupPressed: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Button(
        onClick = {
            onSignupPressed()
            keyboardController?.hide()
        },
    ) {
        Text(text = stringResource(id = R.string.login_sign_up_link_target))
    }
}

@Composable
@Preview
private fun Preview() {
    val initialState = ViewState("Username", "Password")
    CredentialsScreen(
        state = produceState(initialValue = initialState, producer = { value = initialState }),
        onUsernameChanged = {},
        onPasswordChanged = {},
        onForgotPasswordPressed = {},
        onLoginPressed = {},
        onSignupPressed = {}
    )
}
