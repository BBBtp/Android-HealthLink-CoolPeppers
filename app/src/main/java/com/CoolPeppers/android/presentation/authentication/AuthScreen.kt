package com.CoolPeppers.android.presentation.authentication

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.Image
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.CoolPeppers.android.data.model.MockAuthController
import kotlinx.coroutines.launch
import com.CoolPeppers.android.ui.theme.LightBgSecondary
import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.ui.theme.Typography
import com.CoolPeppers.android.R


@Preview(showBackground = true)
@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(MockAuthController())
    )
) {
    var isLoginScreen by remember { mutableStateOf(false) }

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
        if (isLoginScreen) {
            LoginScreen(
                viewModel = viewModel,
                onSwitchToRegister = { isLoginScreen = false }
            )
        } else {
            RegisterScreen(
                viewModel = viewModel,
                onSwitchToLogin = { isLoginScreen = true }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onSwitchToRegister: () -> Unit
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
                    value = viewModel.loginEmail,
                    onValueChange = { viewModel.loginEmail = it },
                    label = { Text("Ваш еmail", color = LightTextPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
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
                    value = viewModel.loginPassword,
                    onValueChange = { viewModel.loginPassword = it },
                    label = { Text("Ваш пароль", color = LightTextPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }
        }


        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.login()
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
            contentDescription = "login choise"
        )
        Button(
            onClick = {
                coroutineScope.launch {
                    // to do
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onSwitchToLogin: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope() // Область видимости для корутин

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
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent, // Прозрачный фон
                        focusedBorderColor = Color.Transparent, // Убираем стандартную рамку
                        unfocusedBorderColor = Color.Transparent // Убираем стандартную рамку
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
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
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
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
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
