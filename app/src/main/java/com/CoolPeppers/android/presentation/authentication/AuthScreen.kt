package com.CoolPeppers.android.presentation.authentication

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.CoolPeppers.android.data.model.MockAuthController
import kotlinx.coroutines.launch
import com.CoolPeppers.android.ui.theme.LightBgSecondary
import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.ui.theme.Typography
import com.CoolPeppers.android.R


@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController,
) {
    var authState by remember {
        mutableStateOf(AuthState.LOGIN)
    }

    IconButton(
        onClick = { /* TODO */ },
        modifier = Modifier
            .size(40.dp)
            .padding(start = 10.dp, top = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.goback),
            contentDescription = "Go back icon",
            modifier = Modifier.fillMaxSize()
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.smileface),
            contentDescription = "smile face",
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
           /* AuthState.FORGOT_PASSWORD -> ForgotPasswordScreen(
                viewModel = viewModel,
                onBackToLogin = { authState = AuthState.LOGIN }
            )*/
        }
    }
}

enum class AuthState {
    LOGIN, REGISTER /*, FORGOT_PASSWORD*/
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onSwitchToRegister: () -> Unit,
    navController: NavController,
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Вход",
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = LightTextPrimary
        )

        OutlinedTextField(
            value = viewModel.loginUsername,
            onValueChange = { viewModel.loginUsername = it },
            label = { Text("Ваш email", color = LightTextPrimary) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = "Email Icon",
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
            label = { Text("Ваш пароль", color = LightTextPrimary) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.password),
                    contentDescription = "Password Icon",
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

        ClickableText(
            text = AnnotatedString("Забыли пароль?"),
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
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBgSecondary
            )
        ) {
            Text(
                text = "Войти",
                color = LightTextPrimary,
                fontSize = 18.sp
            )
        }

        ClickableText(
            text = AnnotatedString("Нет аккаунта? Создай!"),
            onClick = { onSwitchToRegister() },
            style = TextStyle(
                fontSize = 15.sp,
                color = Color.DarkGray,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.padding(top = 8.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.login_choise),
            contentDescription = "login choice"
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.login()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBgSecondary
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Google Icon",
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp)
                        .size(25.dp)
                )
                Text(
                    text = "Войти через Google",
                    color = LightTextPrimary,
                    fontSize = 18.sp
                )
            }
        }
    }
}


@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSwitchToLogin: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Регистрация",
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = LightTextPrimary
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(
                    width = 5.dp, // Толщина рамки
                    color = LightBgSecondary, // Цвет рамки
                    shape = RoundedCornerShape(25.dp) // Закругленные углы
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = "Person Icon",
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .size(20.dp)
                )
                OutlinedTextField(
                    value = viewModel.username,
                    onValueChange = { viewModel.username = it },
                    label = { Text("Ваше имя", color = LightTextPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )
                )
            }
        }

        // Поле для ввода email
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(
                    width = 5.dp, // Толщина рамки
                    color = LightBgSecondary, // Цвет рамки
                    shape = RoundedCornerShape(25.dp) // Закругленные углы
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = "Email Icon",
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .size(20.dp)
                )
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("Ваш еmail", color = LightTextPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )
                )
            }
        }

        // Поле для ввода пароля
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(
                    width = 5.dp, // Толщина рамки
                    color = LightBgSecondary, // Цвет рамки
                    shape = RoundedCornerShape(25.dp) // Закругленные углы
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.password),
                    contentDescription = "Password Icon",
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .size(20.dp)
                )
                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    label = { Text("Ваш пароль", color = LightTextPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )
                )
            }
        }


        Button(
            onClick = {
                coroutineScope.launch { // Запускаем корутину
                    viewModel.register()
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(48.dp), // Высота кнопки
            shape = RoundedCornerShape(25.dp), // Закругленные углы
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBgSecondary // Цвет кнопки
            )
        ) {
            Text(
                text = "Регистрация",
                color = LightTextPrimary,
                fontSize = 18.sp
            )
        }

        ClickableText(
            text = AnnotatedString("Есть аккаунт? Войти"),
            onClick = { onSwitchToLogin() },
            style = TextStyle(
                fontSize = 15.sp,
                color = Color.DarkGray, // Цвет текста
                textDecoration = TextDecoration.Underline // Подчеркивание
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}


/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onBackToLogin: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(bottom = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Восстановление пароля",
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = LightTextPrimary
        )

        // Поле для email
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(
                    width = 5.dp, // Толщина рамки
                    color = LightBgSecondary, // Цвет рамки
                    shape = RoundedCornerShape(25.dp) // Закругленные углы
                )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.email),
                            contentDescription = "Email Icon",
                            modifier = Modifier
                                .padding(start = 15.dp)
                                .size(20.dp)
                        )
                        OutlinedTextField(
                            value = viewModel.forgotPasswordEmail,
                            onValueChange = { viewModel.forgotPasswordEmail = it },
                            label = { Text(text = "Ваш email", color = LightTextPrimary) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(25.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            )
                        )
                    }
                }

                    viewModel.passwordResetError?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            if (viewModel.passwordResetSent) {
                Text(
                    text = "Ссылка отправлена на ваш email",
                    color = Color.Green,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Button(
                onClick = {coroutineScope.launch {
                    viewModel.sendPasswordReset() }
                          },
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(48.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBgSecondary
            )
        ) {
            Text(
                text = "Отправить",
                color = LightTextPrimary,
                fontSize = 18.sp
            )
        }

        ClickableText(
            text = AnnotatedString("Вернуться к входу"),
            onClick = { onBackToLogin() },
            style = TextStyle(
                fontSize = 15.sp,
                color = Color.DarkGray,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
*/
