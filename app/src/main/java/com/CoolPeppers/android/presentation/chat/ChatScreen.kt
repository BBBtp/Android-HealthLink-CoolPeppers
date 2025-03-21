import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.CoolPeppers.android.data.model.Doctor
import com.CoolPeppers.android.data.repository.ClinicRepository
import com.CoolPeppers.android.presentation.navigation.bottomNavigation.BottomNavigationBar
import kotlinx.coroutines.delay



@Composable
fun ChatScreen() {
    val navController = rememberNavController()
    val doctors = remember { mutableStateOf<List<Doctor>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) } // Состояние загрузки

    LaunchedEffect(Unit) {
        doctors.value = ClinicRepository().fetchDoctors()
        isLoading = false // Данные загружены
    }

    NavHost(navController = navController, startDestination = "chatList") {
        composable("chatList") {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF2F6690))
                }
            } else {
                ChatApp(navController, doctors.value)
            }
        }
        composable("chatDialog/{doctorId}") { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId")?.toIntOrNull()
            val doctor = doctors.value.find { it.id == doctorId }
            if (doctor != null) {
                ChatDialogScreen(doctor = doctor, onBack = {
                    navController.popBackStack()
                })
            }
        }
    }
}

@Composable
fun ChatApp(navController: NavController, doctors: List<Doctor>) {
    val context = LocalContext.current
    val searchText = remember { mutableStateOf(TextFieldValue("")) }
    val chats = remember { mutableStateOf<List<Chat>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) } // Состояние загрузки

    LaunchedEffect(Unit) {
        // Создаем список чатов на основе врачей
        chats.value = doctors.mapIndexed { index, doctor ->
            Chat(
                doctor = doctor,
                lastMessage = Message(
                    id = index + 1,
                    text = when (index) {
                        0 -> "У тебя все ок????"
                        1 -> "АЛОООО!!! ОТВЕТЬ"
                        2 -> "Ну как там с деньгами"
                        else -> "Ты кто"
                    },
                    isFromUser = false
                ),
                time = when (index) {
                    0 -> "12:45"
                    1 -> "Вчера"
                    2 -> "5 марта"
                    else -> "11.11.21"
                }
            )
        }
        isLoading = false // Данные загружены
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            // Показываем индикатор загрузки
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF2F6690))
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                BasicTextField(
                    value = searchText.value,
                    onValueChange = { searchText.value = it },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFEAF4F4), CircleShape)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    decorationBox = { innerTextField ->
                        Box {
                            if (searchText.value.text.isEmpty()) {
                                Text("Поиск", color = Color(0xFF808080))
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val filteredDoctors = doctors.filter { doctor ->
                doctor.firstName.contains(searchText.value.text, ignoreCase = true) ||
                        doctor.lastName.contains(searchText.value.text, ignoreCase = true)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filteredDoctors.forEach { doctor ->
                    DoctorAvatar(
                        doctor = doctor,
                        onClick = {
                            navController.navigate("chatDialog/${doctor.id}")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Фильтрация чатов по введенному тексту
            val filteredChats = chats.value.filter { chat ->
                chat.doctor.firstName.contains(searchText.value.text, ignoreCase = true) ||
                        chat.doctor.lastName.contains(searchText.value.text, ignoreCase = true)
            }

            // Список активных чатов
            if (filteredChats.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет активных чатов", fontSize = 18.sp, color = Color(0xFF2F6690))
                }
            } else {
                LazyColumn {
                    items(filteredChats) { chat ->
                        ChatItem(chat = chat, onClick = {
                            navController.navigate("chatDialog/${chat.doctor.id}")
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun DoctorAvatar(doctor: Doctor, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = doctor.image),
            contentDescription = "Doctor Avatar",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Box {
            Text(
                text = "${doctor.firstName} ${doctor.lastName}",
                fontSize = 12.sp,
                color = Color(0XFF2F6690)
            )
            Text(
                text = doctor.specialty,
                fontSize = 10.sp,
                color = Color(0XFF2F6690),
                modifier = Modifier.padding(top = 15.dp)
            )
        }
    }
}
@Composable
fun ChatItem(chat: Chat, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = chat.doctor.image),
                contentDescription = "Doctor Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .padding(horizontal = 10.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    text = "${chat.doctor.firstName} ${chat.doctor.lastName}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F6690)
                )
                Text(
                    text = chat.lastMessage.text,
                    fontSize = 14.sp,
                    color = Color(0xFF2F6690)
                )
            }
        }
        Text(
            text = chat.time,
            fontSize = 12.sp,
            color = Color(0xFF2F6690),
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}

data class Chat(val doctor: Doctor, val lastMessage: Message, val time: String)