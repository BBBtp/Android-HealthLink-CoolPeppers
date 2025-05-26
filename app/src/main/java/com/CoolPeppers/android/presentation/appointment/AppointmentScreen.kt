import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.text.style.TextOverflow
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.SlotResponse
import com.CoolPeppers.android.presentation.appointment.AppointmentViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    clinicId: Int,
    serviceId: Int,
    doctorId: Int,
    viewModel: AppointmentViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val allSlots by viewModel.slots.observeAsState(emptyList())
    val showDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = remember { mutableStateOf<Date?>(null) }
    val selectedSlot = remember { mutableStateOf<SlotResponse?>(null) }

    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())


    val filteredSlots = remember(allSlots, selectedDate.value) {
        if (selectedDate.value == null) emptyList() else {
            val selectedDay = SimpleDateFormat("yyyy-MM-dd").format(selectedDate.value)
            allSlots.filter { it.slotTime.startsWith(selectedDay) }
        }
    }


    LaunchedEffect(doctorId) {
        viewModel.loadSlots(doctorId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .background(Color(0xFFEAF4F4))
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }

            Text(
                text = stringResource(R.string.schedule),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center))
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.select_date),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp))

                Button(
                    onClick = { showDatePicker.value = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2F6690),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = selectedDate.value?.let {
                            dateFormat.format(it)
                        } ?: "Выберите дату",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.select_time),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp))
            }

            if (selectedDate.value == null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.select_date_for_slots),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else if (filteredSlots.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_available_slots),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                items(filteredSlots.chunked(3)) { rowSlots ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowSlots.forEach { slot ->
                            val time = try {
                                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(slot.slotTime)
                                timeFormat.format(date)
                            } catch (e: Exception) {
                                slot.slotTime.substringAfterLast("T")
                            }

                            TimeSlotButton(
                                time = time,
                                isSelected = selectedSlot.value?.id == slot.id,
                                onClick = {
                                    selectedSlot.value = slot
                                }
                            )
                        }
                        repeat(3 - rowSlots.size) {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    when {
                        selectedDate.value == null -> {
                            Toast.makeText(context, "Пожалуйста, выберите дату", Toast.LENGTH_SHORT).show()
                        }
                        selectedSlot.value == null -> {
                            Toast.makeText(context, "Пожалуйста, выберите время", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            selectedSlot.value?.let { slot ->
                                navController.navigate("payment/${clinicId}/${serviceId}/${doctorId}/${slot.id}")
                            }

                        }
                    }
                },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEAF4F4), contentColor = Color(0xFF2F6690)),
            modifier = Modifier
                .fillMaxWidth()
                .height(49.dp)
            ) {
            Text(
                text = stringResource(R.string.next),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        }
    }

    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            selectedDate.value = Date(it)
                            selectedSlot.value = null
                            showDatePicker.value = false
                        }
                    }
                ) {
                    Text("OK", color = Color(0xFF2F6690))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker.value = false }
                ) {
                    Text("Отмена", color = Color(0xFF2F6690))
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF2F6690),
                    headlineContentColor = Color(0xFF2F6690),
                    weekdayContentColor = Color(0xFF2F6690),
                    subheadContentColor = Color(0xFF2F6690),
                    navigationContentColor = Color(0xFF2F6690),
                    yearContentColor = Color(0xFF2F6690),
                    currentYearContentColor = Color(0xFF2F6690),
                    selectedYearContentColor = Color.White,
                    selectedYearContainerColor = Color(0xFF2F6690),
                    dayContentColor = Color(0xFF2F6690),
                    disabledDayContentColor = Color.LightGray,
                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = Color(0xFF2F6690),
                    todayContentColor = Color(0xFF2F6690),
                    todayDateBorderColor = Color(0xFF2F6690)
                )
            )
        }
    }
}

@Composable
fun TimeSlotButton(
    time: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF2F6690) else Color.White,
            contentColor = if (isSelected) Color.White else Color(0xFF123143)
        ),
        border = if (isSelected) null else ButtonDefaults.outlinedButtonBorder,
        modifier = Modifier
            .width(100.dp)
            .height(32.dp)
    ) {
        Text(
            text = time,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}