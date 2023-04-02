@file:OptIn(ExperimentalComposeUiApi::class)

package com.chesire.nekome.app.login.credentials.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chesire.nekome.app.login.R
import com.chesire.nekome.core.compose.theme.NekomeTheme

@Composable
fun CredentialsScreen(
    viewModel: CredentialsViewModel = hiltViewModel(),
    finishAction: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()

    val navigate = state.value.navigateScreenEvent
    if (navigate == true) {
        LaunchedEffect(navigate) {
            finishAction()
            viewModel.execute(ViewAction.NavigationObserved)
        }
    }

    Render(
        state = state,
        onUsernameChanged = { viewModel.execute(ViewAction.UsernameChanged(it)) },
        onPasswordChanged = { viewModel.execute(ViewAction.PasswordChanged(it)) },
        onLoginPressed = { viewModel.execute(ViewAction.LoginPressed) },
        onSnackbarShown = { viewModel.execute(ViewAction.ErrorSnackbarObserved) }
    )
}

@Composable
private fun Render(
    state: State<UIState>,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginPressed: () -> Unit,
    onSnackbarShown: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.semantics { testTag = CredentialsTags.Snackbar }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .semantics { testTag = CredentialsTags.Root }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderImage()
            UsernameInput(
                username = state.value.username,
                isUsernameError = state.value.hasUsernameError,
                onUsernameChanged = onUsernameChanged
            )
            PasswordInput(
                password = state.value.password,
                isLoggingIn = state.value.isPerformingLogin,
                isPasswordError = state.value.hasPasswordError,
                onPasswordChanged = onPasswordChanged,
                onLoginPressed = onLoginPressed
            )
            ForgotPasswordButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            if (state.value.isPerformingLogin) {
                CircularProgressIndicator()
            } else {
                LoginButton(
                    isEnabled = state.value.loginButtonEnabled,
                    isLoggingIn = state.value.isPerformingLogin,
                    onLoginPressed = onLoginPressed
                )
                SignupButton()
            }
        }
    }

    val snackbarMessage = state.value.errorSnackbarMessage
    if (snackbarMessage != null) {
        val message = stringResource(id = snackbarMessage)
        LaunchedEffect(snackbarMessage) {
            snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
            onSnackbarShown()
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
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        isError = isUsernameError,
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.login_username)) },
        modifier = Modifier.semantics {
            testTag = CredentialsTags.Username
        }
    )
}

@Composable
private fun PasswordInput(
    password: String,
    isLoggingIn: Boolean,
    isPasswordError: Boolean,
    onPasswordChanged: (String) -> Unit,
    onLoginPressed: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
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
                    contentDescription = stringResource(
                        id = if (passwordVisible) {
                            R.string.login_hide_password
                        } else {
                            R.string.login_show_password
                        }
                    )
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                if (!isLoggingIn) {
                    onLoginPressed()
                    keyboardController?.hide()
                }
            }
        ),
        isError = isPasswordError,
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.login_password)) },
        modifier = Modifier.semantics {
            testTag = CredentialsTags.Password
        }
    )
}

@Composable
private fun ForgotPasswordButton(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val forgotPasswordUrl = "https://kitsu.io/password-reset"

    TextButton(
        modifier = modifier,
        onClick = { uriHandler.openUri(forgotPasswordUrl) }
    ) {
        Text(text = stringResource(id = R.string.login_forgot_password))
    }
}

@Composable
private fun LoginButton(isEnabled: Boolean, isLoggingIn: Boolean, onLoginPressed: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Button(
        enabled = isEnabled,
        onClick = {
            if (!isLoggingIn) {
                onLoginPressed()
                keyboardController?.hide()
            }
        }
    ) {
        Text(text = stringResource(id = R.string.login_login))
    }
}

@Composable
private fun SignupButton() {
    val keyboardController = LocalSoftwareKeyboardController.current
    val uriHandler = LocalUriHandler.current
    val signupUrl = "https://kitsu.io/explore/anime"

    TextButton(
        onClick = {
            uriHandler.openUri(signupUrl)
            keyboardController?.hide()
        }
    ) {
        Text(text = stringResource(id = R.string.login_sign_up_kitsu))
    }
}

@Composable
@Preview
private fun Preview() {
    val initialState = UIState(
        username = "Username",
        hasUsernameError = false,
        password = "Password",
        hasPasswordError = false,
        isPerformingLogin = false,
        loginButtonEnabled = true,
        errorSnackbarMessage = null,
        navigateScreenEvent = null
    )
    NekomeTheme(darkTheme = true) {
        Render(
            state = produceState(initialValue = initialState, producer = { value = initialState }),
            { /**/ },
            { /**/ },
            { /**/ },
            { /**/ }
        )
    }
}

object CredentialsTags {
    const val Root = "CredentialsRoot"
    const val Username = "CredentialsUsername"
    const val Password = "CredentialsPassword"
    const val Snackbar = "CredentialsSnackbar"
}
