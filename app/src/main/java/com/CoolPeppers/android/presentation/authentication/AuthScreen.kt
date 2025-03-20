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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.CoolPeppers.android.data.model.MockAuthController
import kotlinx.coroutines.launch
import com.CoolPeppers.android.ui.theme.LightBgSecondary
import com.CoolPeppers.android.ui.theme.LightTextPrimary


@Preview(showBackground = true)
@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(MockAuthController())
    )
) {
    var isLoginScreen by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
    val coroutineScope = rememberCoroutineScope() // Область видимости для корутин

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
            OutlinedTextField(
                value = viewModel.loginEmail,
                onValueChange = { viewModel.loginEmail = it },
                label = { Text("Ваш еmail") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
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
            OutlinedTextField(
                value = viewModel.loginPassword,
                onValueChange = { viewModel.loginPassword = it },
                label = { Text("Ваш пароль") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }


        Button(
            onClick = {
                coroutineScope.launch { // Запускаем корутину
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
                fontSize = 14.sp,
                color = Color.DarkGray, // Цвет текста
                textDecoration = TextDecoration.Underline // Подчеркивание
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
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
        // Поле для ввода имени
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
            OutlinedTextField(
                value = viewModel.username,
                onValueChange = { viewModel.username = it },
                label = { Text("Ваше имя") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent, // Прозрачный фон
                    focusedBorderColor = Color.Transparent, // Убираем стандартную рамку
                    unfocusedBorderColor = Color.Transparent // Убираем стандартную рамку
                )
            )
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
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("Ваш еmail") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
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
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text("Ваш пароль") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
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
                fontSize = 14.sp,
                color = Color.DarkGray, // Цвет текста
                textDecoration = TextDecoration.Underline // Подчеркивание
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
