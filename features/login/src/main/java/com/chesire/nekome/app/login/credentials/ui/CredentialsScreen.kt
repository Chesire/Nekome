@file:OptIn(ExperimentalComposeUiApi::class)

package com.chesire.nekome.app.login.credentials.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chesire.core.compose.theme.NekomeTheme
import com.chesire.nekome.app.login.R
import com.chesire.nekome.core.url.UrlHandler

@Composable
fun CredentialsScreen(
    urlHandler: UrlHandler,
    viewModel: CredentialsViewModel = viewModel()
) {
    val state = viewModel.viewState.collectAsState()
    val context = LocalContext.current
    val signupUrl = stringResource(id = R.string.login_sign_up_url)
    val forgotPasswordUrl = stringResource(id = R.string.login_forgot_password_url)

    Render(
        state = state,
        onUsernameChanged = { viewModel.execute(ViewAction.UsernameChanged(it)) },
        onPasswordChanged = { viewModel.execute(ViewAction.PasswordChanged(it)) },
        onForgotPasswordPressed = { urlHandler.launch(context, forgotPasswordUrl) },
        onLoginPressed = { viewModel.execute(ViewAction.LoginPressed) },
        onSignupPressed = { urlHandler.launch(context, signupUrl) }
    )
}

@Composable
private fun Render(
    state: State<ViewState>,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onForgotPasswordPressed: () -> Unit,
    onLoginPressed: () -> Unit,
    onSignupPressed: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderImage()
            UsernameInput(state.value.username, state.value.usernameError, onUsernameChanged)
            PasswordInput(
                state.value.password,
                state.value.passwordError,
                onPasswordChanged,
                onLoginPressed
            )
            ForgotPasswordButton(
                Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp),
                onForgotPasswordPressed
            )
            Spacer(modifier = Modifier.weight(1f))
            LoginButton(state.value.buttonEnabled, onLoginPressed)
            SignupButton(onSignupPressed)
        }
    }
}

@Composable
private fun HeaderImage() {
    Image(
        painter = painterResource(id = R.drawable.blackcat),
        contentDescription = null,
        contentScale = ContentScale.Inside,
        modifier = Modifier
            .padding(16.dp)
            .width(160.dp)
    )
}

@Composable
private fun UsernameInput(
    username: String,
    isUsernameError: Boolean,
    onUsernameChanged: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChanged,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
        ),
        isError = isUsernameError,
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.login_username)) }
    )
}

@Composable
private fun PasswordInput(
    password: String,
    isPasswordError: Boolean,
    onPasswordChanged: (String) -> Unit,
    onLoginPressed: () -> Unit
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChanged,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = stringResource(id = if (passwordVisible) R.string.login_hide_password else R.string.login_show_password)
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onLoginPressed() },
        ),
        isError = isPasswordError,
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.login_password)) }
    )
}

@Composable
private fun ForgotPasswordButton(
    modifier: Modifier = Modifier,
    onForgotPasswordPressed: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onForgotPasswordPressed
    ) {
        Text(text = stringResource(id = R.string.login_forgot_password))
    }
}

@Composable
private fun LoginButton(isEnabled: Boolean, onLoginPressed: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Button(
        enabled = isEnabled,
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

    TextButton(
        onClick = {
            onSignupPressed()
            keyboardController?.hide()
        },
    ) {
        Text(text = stringResource(id = R.string.login_sign_up_kitsu))
    }
}

@Composable
@Preview
private fun Preview() {
    val initialState = ViewState(
        username = "Username",
        usernameError = false,
        password = "Password",
        passwordError = false,
        isPerformingLogin = false,
        buttonEnabled = true
    )
    NekomeTheme(darkTheme = true) {
        Render(
            state = produceState(initialValue = initialState, producer = { value = initialState }),
            { /**/ },
            { /**/ },
            { /**/ },
            { /**/ },
            { /**/ }
        )
    }
}
