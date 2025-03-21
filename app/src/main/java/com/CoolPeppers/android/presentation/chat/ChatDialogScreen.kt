
import androidx.annotation.Nullable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Doctor
import com.CoolPeppers.android.data.model.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val LocalBottomBarVisibility = staticCompositionLocalOf { mutableStateOf(true) }

@Composable
fun ChatDialogScreen(doctor: Doctor, onBack: () -> Unit) {
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Message>() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val bottomBarVisibility = LocalBottomBarVisibility.current

    // Скрываем нижний бар при входе на экран
    DisposableEffect(Unit) {
        bottomBarVisibility.value = false
        onDispose {
            bottomBarVisibility.value = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Заголовок с кнопкой "Назад" и аватаркой врача
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back_btn),
                    contentDescription = "Назад",
                    modifier = Modifier.size(29.dp)
                )
            }
            Image(
                painter = painterResource(id = doctor.image),
                contentDescription = "Doctor Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${doctor.firstName} ${doctor.lastName}",
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Text(
                    text = doctor.specialty,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        // Список сообщений
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message)
            }
        }

        // Поле ввода сообщения и кнопка отправки
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(Color(0xFFEAF4F4)),
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .height(65.dp)
                        .padding(end = 10.dp)
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(24.dp)
                        ),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.padding(start = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { /* Логика для смайликов */ },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.emoji_btn),
                                contentDescription = "Смайлики",
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Поле ввода текста
                        TextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            modifier = Modifier
                                .weight(1f),
                            placeholder = { Text(text = "Введите сообщение...", fontSize = 12.sp) },
                            shape = RoundedCornerShape(24.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Default,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    if (messageText.isNotBlank()) {
                                        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(
                                            Date()
                                        ) // Текущее время
                                        messages.add(Message(messages.size + 1, messageText, currentTime, true))
                                        messageText = ""
                                        keyboardController?.hide()
                                    }
                                }
                            )
                        )

                        IconButton(
                            onClick = { /* Логика для прикрепления файла */ },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.file_btn),
                                contentDescription = "Прикрепить файл",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Кнопка отправки сообщения
                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) // Текущее время
                            messages.add(Message(messages.size + 1, messageText, currentTime, true))
                            messageText = ""
                            keyboardController?.hide()
                        }
                    },
                    modifier = Modifier
                        .size(53.dp)
                        .background(Color(0xFF2F6690), RoundedCornerShape(50))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.send_icon),
                        contentDescription = "Отправить",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun MessageBubble(message: Message) {
    val alignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    val bgColor = if (message.isFromUser) Color(0xFFFFFFFF) else Color(0xFF2F6690) // Цвет фона пузыря
    val textColor = if (message.isFromUser) Color(0xFF2F6690) else Color(0xFFFFFFFF) // Цвет текста
    val timeAlignment = if (message.isFromUser) Alignment.Start else Alignment.End // Выравнивание времени

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Column(
            horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
        ) {
            Surface(
                color = bgColor,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = textColor,
                    fontSize = 16.sp
                )
            }

            Text(
                text = message.time,
                fontSize = 12.sp,
                color = textColor,
                modifier = Modifier
                    .padding(top = 4.dp, start = 16.dp, end = 16.dp)
                    .align(timeAlignment)
            )
        }
    }
}

