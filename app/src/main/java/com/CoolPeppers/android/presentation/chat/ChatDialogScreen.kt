
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.graphics.colorspace.Rgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Doctor

data class Message(val id: Int, val text: String, val isFromUser: Boolean)
val LocalBottomBarVisibility = staticCompositionLocalOf { mutableStateOf(true) }

@Composable
fun ChatDialogScreen(doctor: Doctor, onBack: () -> Unit) {
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Message>() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val bottomBarVisibility = LocalBottomBarVisibility.current // Получаем состояние видимости нижнего бара

    // Скрываем нижний бар при входе на экран
    DisposableEffect(Unit) {
        bottomBarVisibility.value = false // Скрываем нижний бар
        onDispose {
            bottomBarVisibility.value = true // Показываем нижний бар при выходе
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

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

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message)
            }
        }

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
                    color = Color.Transparent // Прозрачный фон
                ) {
                    Row(
                        modifier = Modifier.padding(start = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(
                            onClick = {
                            },
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
                            placeholder = { Text(text ="Введите сообщение...", fontSize = 12.sp )},
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
                                        messages.add(Message(messages.size + 1, messageText, true))
                                        messageText = ""
                                        keyboardController?.hide()
                                    }
                                }
                            )
                        )

                        IconButton(
                            onClick = {
                                // Логика для прикрепления файла
                            },
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

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            messages.add(Message(messages.size + 1, messageText, true))
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
    val bgcolor = if (!message.isFromUser) Color(0xFF2F6690) else Color(0xFFFFFFFF)
    val color = if (message.isFromUser) Color(0xFF2F6690) else Color(0xFFFFFFFF)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Surface(
            color = bgcolor,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = color,
                fontSize = 16.sp
            )
        }
    }
}

