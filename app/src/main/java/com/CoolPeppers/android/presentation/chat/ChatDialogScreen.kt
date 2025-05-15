
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Chat
import com.CoolPeppers.android.data.model.Message
import com.CoolPeppers.android.presentation.chat.ChatDialogViewModel
import com.CoolPeppers.android.ui.theme.LocalBottomBarVisibility
import com.CoolPeppers.android.util.MessageTimeFormatter
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ChatDialogScreen(
    chat: Chat,
    onBack: () -> Unit
) {
    val viewModel: ChatDialogViewModel = hiltViewModel()
    var messageText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val bottomBarVisibility = LocalBottomBarVisibility.current
    val connectionState by viewModel.connectionState
    val loadingState by viewModel.loadingState
    val lazyListState = rememberLazyListState()
    val messages by viewModel.messagesFlow.collectAsState()

    fun List<Message>.groupByDate(): List<Any> {
        val grouped = mutableListOf<Any>()
        var currentDate = ""

        this.forEach { message ->
            val messageDate = MessageTimeFormatter.formatDate(message.created_at)
            if (messageDate != currentDate) {
                grouped.add(messageDate)
                currentDate = messageDate
            }
            grouped.add(message)
        }

        return grouped
    }
    DisposableEffect(Unit) {
        bottomBarVisibility.value = false
        onDispose {
            bottomBarVisibility.value = true
        }
    }

    if (loadingState) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    if (!connectionState) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Нет соединения", color = Color.Red)
                Button(onClick = { viewModel.connectWebSocket() }) {
                    Text("Повторить попытку")
                }
            }
        }
        return
    }
    if (viewModel.currentUserId == 0) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {
                Text("Ошибка загрузки данных чата")
                Button(onClick = { viewModel.loadChatParticipants() }) {
                    Text("Повторить")
                }
            }
        }
        return
    }
    Column(modifier = Modifier.fillMaxSize()) {
        ChatHeader(chat, onBack)

        Box(modifier = Modifier.weight(1f)) {
            val groupedMessages = remember(messages) { messages.groupByDate() }

            if (groupedMessages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Text(
                            text = "Здесь пока ничего нет...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2F6690)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Отправьте сообщение.",
                            fontSize = 18.sp,
                            color = Color(0xFF2F6690)
                        )
                    }

                }
            } else {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    reverseLayout = false
                ) {
                    items(groupedMessages) { item ->
                        when (item) {
                            is String -> DateSeparator(date = item)
                            is Message -> MessageBubble(
                                message = item,
                                isCurrentUser = item.sender_id == viewModel.currentUserId,
                                onStatusUpdate = { status ->
                                    viewModel.updateMessageStatus(item.id, status)
                                },
                            )
                        }
                    }
                }

                FloatingDateHeader(lazyListState, groupedMessages)
            }
        }

        MessageInput(
            messageText = messageText,
            onMessageChange = { messageText = it },
            onSend = {
                if (messageText.isNotBlank()) {
                    viewModel.sendMessage(messageText)
                    messageText = ""
                    keyboardController?.hide()
                }
            },
            enabled = connectionState
        )
    }

    // Автоматическая прокрутка при изменении списка сообщений
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(messages.lastIndex)
        }
    }

    // Прокрутка при первом открытии
    LaunchedEffect(Unit) {
        if (messages.isNotEmpty()) {
            lazyListState.scrollToItem(messages.lastIndex)
        }
    }
}

@Composable
private fun ChatHeader(chat: Chat, onBack: () -> Unit) {
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
        AsyncImage(
            model = chat.user2.photoUrl,
            contentDescription = "Doctor Avatar",
            modifier = Modifier
                .size(56.dp)
                .padding(end = 10.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.smileface),
            error = painterResource(R.drawable.smileface)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "${chat.user2.firstName} ${chat.user2.lastName}",
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun DateSeparator(date: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFEAF4F4),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = date,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                color = Color(0xFF2F6690),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun FloatingDateHeader(
    listState: LazyListState,
    groupedMessages: List<Any>
) {
    var lastFirstVisibleItem by remember { mutableStateOf(0) }
    var isScrolling by remember { mutableStateOf(false) }
    val alpha = animateFloatAsState(
        targetValue = if (isScrolling) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "date_alpha"
    )

    // Отслеживаем изменение позиции скролла
    LaunchedEffect(listState.firstVisibleItemIndex) {
        val currentFirst = listState.firstVisibleItemIndex
        if (currentFirst != lastFirstVisibleItem) {
            isScrolling = true
            lastFirstVisibleItem = currentFirst
        } else {
            isScrolling = false
        }
    }

    val visibleItems = remember(listState) {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo
                .map { it.index }
                .takeIf { it.isNotEmpty() }
                ?.let { it.min()..it.max() }
        }
    }

    val currentDate = remember(visibleItems.value, groupedMessages) {
        derivedStateOf {
            val dateIndices = groupedMessages
                .mapIndexedNotNull { index, item ->
                    if (item is String) index else null
                }

            val visibleDates = dateIndices
                .filter { visibleItems.value?.contains(it) == true }

            groupedMessages
                .asReversed()
                .filterIsInstance<String>()
                .firstOrNull { date ->
                    val dateIndex = groupedMessages.indexOf(date)
                    dateIndex < (visibleItems.value?.start ?: 0) &&
                            dateIndex !in visibleDates
                }
        }
    }

    if (currentDate.value != null && alpha.value > 0.01f) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .graphicsLayer(alpha = alpha.value),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF2F6690),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = currentDate.value!!,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }

    // Автоматическое скрытие через 2 секунды после остановки скролла
    LaunchedEffect(isScrolling) {
        if (isScrolling) {
            delay(2000)
            isScrolling = false
        }
    }
}

@Composable
private fun MessageInput(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit,
    enabled: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEAF4F4)),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .height(65.dp)
                    .padding(end = 10.dp)
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = if (enabled) Color.Black else Color.Gray,
                        shape = RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier.padding(start = 9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = onMessageChange,
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Введите сообщение...", fontSize = 12.sp) },
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Send,
                            keyboardType = KeyboardType.Text
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = { if (enabled) onSend() }
                        ),
                        enabled = enabled
                    )
                }
            }

            IconButton(
                onClick = onSend,
                modifier = Modifier
                    .size(53.dp)
                    .background(
                        color = if (enabled) Color(0xFF2F6690) else Color.Gray,
                        shape = RoundedCornerShape(50)
                    ),
                enabled = enabled && messageText.isNotBlank()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.send_icon),
                    contentDescription = "Отправить",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: Message,
    isCurrentUser: Boolean,
    onStatusUpdate: (String) -> Unit,
) {
    Log.i("Current", "$isCurrentUser")
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val bgColor = if (isCurrentUser) Color(0xFFFFFFFF) else Color(0xFF2F6690)
    val textColor = if (isCurrentUser) Color(0xFF2F6690) else Color(0xFFFFFFFF)
    val formattedTime = remember(message.created_at) {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
            val date = inputFormat.parse(message.created_at)
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        } catch (e: Exception) {
            message.created_at
        }
    }

    LaunchedEffect(message.status) {
        if (isCurrentUser && message.status == "sending") {
            delay(1500)
            onStatusUpdate("delivered")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 16.dp),
        contentAlignment = alignment
    ) {
        Surface(
            color = bgColor,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .wrapContentWidth()
                .align(if (isCurrentUser) Alignment.TopEnd else Alignment.TopStart)
                .widthIn(max = 300.dp)
        ) {
            Box(
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = message.text,
                        color = textColor,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .padding(end = 4.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = formattedTime,
                            fontSize = 12.sp,
                            modifier = Modifier.offset(y = 6.dp),
                            color = textColor.copy(alpha = 0.7f),
                        )
                        if (isCurrentUser) {
                            Icon(
                                painter = painterResource(
                                    when (message.status) {
                                        "read" -> R.drawable.tooth
                                        "delivered" -> R.drawable.tooth
                                        else -> R.drawable.broke
                                    }
                                ),
                                contentDescription = "Status",
                                tint = textColor.copy(alpha = 0.7f),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

