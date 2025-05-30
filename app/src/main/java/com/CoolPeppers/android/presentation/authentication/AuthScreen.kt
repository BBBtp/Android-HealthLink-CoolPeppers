package com.CoolPeppers.android.presentation.authentication

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.Image
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.CoolPeppers.android.data.model.MockAuthController
import kotlinx.coroutines.launch
import com.CoolPeppers.android.ui.theme.Typography
import com.CoolPeppers.android.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController,
) {
    var authState by remember {
        mutableStateOf(AuthState.LOGIN)
    }

    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()



    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.smileface),
                contentDescription = stringResource(R.string.avatar),
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .size(66.dp)
            )
            when (authState) {
                AuthState.LOGIN -> LoginScreen(
                    viewModel = viewModel,
                    onSwitchToRegister = { authState = AuthState.REGISTER },
                    navController = navController
                )
                AuthState.REGISTER -> RegisterScreen(
                    viewModel = viewModel,
                    onSwitchToLogin = { authState = AuthState.LOGIN }
                )
            }
        }

        // Показываем ошибку, если она есть
        errorMessage?.let { error ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text(error)
            }
        }

        // Показываем индикатор загрузки
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
            )
        }
    }
}

enum class AuthState {
    LOGIN, REGISTER /*, FORGOT_PASSWORD*/
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onSwitchToRegister: () -> Unit,
    navController: NavController,
) {
    val coroutineScope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }
    val layoutDirection = LocalLayoutDirection.current

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.login),
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        OutlinedTextField(
            value = viewModel.loginUsername,
            onValueChange = { viewModel.loginUsername = it },
            label = { Text(stringResource(R.string.email)) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = stringResource(R.string.email),
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        )

        OutlinedTextField(
            value = viewModel.loginPassword,
            onValueChange = { viewModel.loginPassword = it },
            label = { Text(stringResource(R.string.password)) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.password),
                    contentDescription = stringResource(R.string.password),
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Close else Icons.Default.Info,
                        contentDescription = if (passwordVisible) 
                            stringResource(R.string.hide_password) 
                        else 
                            stringResource(R.string.show_password)
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        )

        ClickableText(
            text = AnnotatedString(stringResource(R.string.forgot_password)),
            onClick = { },
            style = TextStyle(
                fontSize = 15.sp,
                color = Color.DarkGray,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier
                .padding(bottom = 5.dp, end = 5.dp)
                .align(Alignment.End)
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.login()
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(48.dp),
            shape = RoundedCornerShape(25.dp),
            enabled = !viewModel.isLoading.value
        ) {
            Text(
                text = stringResource(R.string.login_button),
                fontSize = 18.sp
            )
        }

        ClickableText(
            text = AnnotatedString(stringResource(R.string.no_account)),
            onClick = { onSwitchToRegister() },
            style = TextStyle(
                fontSize = 15.sp,
                color = Color.DarkGray,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.padding(top = 8.dp)
        )

    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSwitchToLogin: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }
    val layoutDirection = LocalLayoutDirection.current

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.register),
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        OutlinedTextField(
            value = viewModel.username,
            onValueChange = { viewModel.username = it },
            label = { Text(stringResource(R.string.username)) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = stringResource(R.string.username),
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        )

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text(stringResource(R.string.email)) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = stringResource(R.string.email),
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        )

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text(stringResource(R.string.password)) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.password),
                    contentDescription = stringResource(R.string.password),
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Close else Icons.Default.Info,
                        contentDescription = if (passwordVisible) 
                            stringResource(R.string.hide_password) 
                        else 
                            stringResource(R.string.show_password)
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.register()
                    onSwitchToLogin()
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(48.dp),
            shape = RoundedCornerShape(25.dp),
            enabled = !viewModel.isLoading.value
        ) {
            Text(
                text = stringResource(R.string.register),
                fontSize = 18.sp
            )
        }

        ClickableText(
            text = AnnotatedString(stringResource(R.string.have_account)),
            onClick = { onSwitchToLogin() },
            style = TextStyle(
                fontSize = 15.sp,
                color = Color.DarkGray,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

