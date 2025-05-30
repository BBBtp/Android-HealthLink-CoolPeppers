import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Chat
import com.CoolPeppers.android.presentation.chat.ChatViewModel
import com.CoolPeppers.android.util.MessageTimeFormatter
import com.CoolPeppers.android.ui.theme.Typography


@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val chats by viewModel.chats.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshData()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
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
                ChatApp(
                    navController = navController,
                    chats = chats,
                )
            }
        }
        composable("chatDialog/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")?.toIntOrNull()
            val chat = chats.find { it.id == chatId }
            if (chat != null) {
                ChatDialogScreen(
                    chat = chat,
                    onBack = {
                        navController.popBackStack()
                        viewModel.refreshData()
                    },
                )
            }
        }
    }
}

//@Preview
@Composable
fun ChatApp(
    navController: NavController,
    chats: List<Chat>,
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
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
//                    .clip(CircleShape)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                decorationBox = { innerTextField ->
                    Box {
                        if (searchText.value.text.isEmpty()) {
                            Text(stringResource(R.string.search), /*color = Color(0xFF808080)*/)
                        }
                        innerTextField()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val emptyChats = remember(chats) {
            chats.filter { it.messages.isEmpty() }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (emptyChats.isNotEmpty()) {
                emptyChats.forEach { chat ->
                    EmptyChatItem(
                        chat = chat,
                        onClick = {
                            navController.navigate("chatDialog/${chat.id}")
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        val (matchedByUser, matchedByMessage) = remember(chats, searchText.value.text) {
            val searchQuery = searchText.value.text
            val sortedChats = chats.sortedByDescending {
                it.messages.lastOrNull()?.created_at
            }
            val allChats = sortedChats.filter { it.messages.isNotEmpty() }

            val byUser = mutableListOf<Chat>()
            val byMessage = mutableListOf<Chat>()

            if(searchQuery.isNotEmpty()) {
                allChats.forEach { chat ->
                    val userMatch = chat.user2.run {
                        firstName?.contains(searchQuery, ignoreCase = true) == true ||
                                lastName?.contains(searchQuery, ignoreCase = true) == true
                    }

                    val messageMatch = chat.messages.any { message ->
                        message.text?.contains(searchQuery, ignoreCase = true) == true
                    }

                    when {
                        userMatch -> byUser.add(chat)
                        messageMatch -> byMessage.add(chat)
                    }
                }
            } else {
                byUser.addAll(allChats)
            }

            Pair(byUser, byMessage)
        }
        if (searchText.value.text.isNotEmpty() && matchedByUser.isNotEmpty()) {
            Text(
                text = stringResource(R.string.chats),
                style = Typography.titleLarge
            )
            LazyColumn {
                items(matchedByUser) { chat ->
                    ChatItem(
                        chat = chat,
                        onClick = { navController.navigate("chatDialog/${chat.id}") },
                        showLastMessage = true
                    )
                }
            }
        }
        if (searchText.value.text.isEmpty() && matchedByUser.isNotEmpty()) {
            LazyColumn {
                items(matchedByUser) { chat ->
                    ChatItem(
                        chat = chat,
                        onClick = { navController.navigate("chatDialog/${chat.id}") },
                        showLastMessage = true
                    )
                }
            }
        }

        // Секция сообщений
        if(matchedByMessage.isNotEmpty()) {
            Text(
                text = stringResource(R.string.messages),
                style = Typography.titleLarge
            )
            LazyColumn {
                items(matchedByMessage) { chat ->
                    ChatItem(
                        chat = chat,
                        onClick = { navController.navigate("chatDialog/${chat.id}") },
                        showLastMessage = false,
                        searchQuery = searchText.value.text
                    )
                }
            }
        }

        // Пустое состояние
        if(searchText.value.text.isNotEmpty()
            && matchedByUser.isEmpty()
            && matchedByMessage.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.nothing_found),
                    style = Typography.bodyMedium
                )
            }
        }
    }
}


@Composable
fun EmptyChatItem(chat: Chat, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.doctor),
            contentDescription = stringResource(R.string.doctor_avatar),
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Text(
            text = stringResource(R.string.doctor).format(chat.user2.firstName, chat.user2.lastName),
            style = Typography.titleMedium
        )
    }
}

@Composable
fun ChatItem(
    chat: Chat,
    onClick: () -> Unit,
    showLastMessage: Boolean,
    searchQuery: String = ""
) {
    val messageToShow = remember(chat, searchQuery) {
        if(showLastMessage) {
            chat.messages.lastOrNull()
        } else {
            chat.messages.firstOrNull {
                it.text?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }
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
        )
        {
            Icon(
                painter = painterResource(id = R.drawable.doctor),
                contentDescription = stringResource(R.string.doctor_avatar),
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.doctor).format(chat.user2.firstName, chat.user2.lastName),
                    style = Typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
                Text(
                    text = messageToShow?.text ?: "Нет сообщений",
                    modifier = Modifier.padding(end = 13.dp),
                    style = Typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.secondary,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        messageToShow?.let { message ->
            Text(
                text = message.created_at?.let {
                    MessageTimeFormatter.formatSmartDateTime(it)
                } ?: "",
                style = Typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}
