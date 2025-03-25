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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.CoolPeppers.android.data.model.Doctor
import com.CoolPeppers.android.data.model.Chat
import com.CoolPeppers.android.data.model.Message
import com.CoolPeppers.android.data.repository.ClinicRepository
import com.CoolPeppers.android.presentation.chat.ChatViewModel
import com.CoolPeppers.android.presentation.chat.ChatViewModelFactory
import com.CoolPeppers.android.presentation.navigation.bottomNavigation.BottomNavigationBar
import kotlinx.coroutines.delay



@Composable
fun ChatScreen() {
    val navController = rememberNavController()
    val viewModel: ChatViewModel = viewModel(

    )
    val doctors by viewModel.doctors.collectAsState()
    val chats by viewModel.chats.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

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
                ChatApp(navController, doctors, chats)
            }
        }
        composable("chatDialog/{doctorId}") { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId")?.toIntOrNull()
            val doctor = viewModel.getDoctorById(doctorId ?: -1)
            if (doctor != null) {
                ChatDialogScreen(
                    doctor = doctor,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun ChatApp(
    navController: NavController,
    doctors: List<Doctor>,
    chats: List<Chat>
) {
    val searchText = remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
            horizontalArrangement = Arrangement.Center
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

        val filteredChats = chats.filter { chat ->
            chat.doctor.firstName.contains(searchText.value.text, ignoreCase = true) ||
                    chat.doctor.lastName.contains(searchText.value.text, ignoreCase = true)
        }

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
                    ChatItem(
                        chat = chat,
                        onClick = {
                            navController.navigate("chatDialog/${chat.doctor.id}")
                        }
                    )
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
//        Image(
//            painter = painterResource(id = doctor.image),
//            contentDescription = "Doctor Avatar",
//            modifier = Modifier
//                .size(64.dp)
//                .clip(CircleShape),
//            contentScale = ContentScale.Crop
//        )
        // Имя врача с обрезанием текста
        Text(
            text = "${doctor.firstName} ${doctor.lastName}",
            fontSize = 12.sp,
            color = Color(0xFF2F6690),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(80.dp)
        )
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
//            Image(
//                painter = painterResource(id = chat.doctor.image),
//                contentDescription = "Doctor Avatar",
//                modifier = Modifier
//                    .size(56.dp)
//                    .padding(end = 10.dp)
//                    .clip(CircleShape),
//                contentScale = ContentScale.Crop
//            )
            Column(modifier = Modifier.weight(1f)) {
                // Имя врача с обрезанием текста
                Text(
                    text = "${chat.doctor.firstName} ${chat.doctor.lastName}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F6690),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                // Последнее сообщение с обрезанием текста
                Text(
                    text = chat.lastMessage.text,
                    modifier = Modifier.padding(end = 13.dp),
                    fontSize = 14.sp,
                    color = Color(0xFF2F6690),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Text(
            text = chat.lastMessage.time,
            fontSize = 12.sp,
            color = Color(0xFF2F6690),
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}
