package com.CoolPeppers.android.presentation.notifications

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import com.CoolPeppers.android.presentation.home.components.RecordCard
import com.CoolPeppers.android.presentation.home.HomeViewModel
import com.CoolPeppers.android.presentation.doctor.DoctorViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "NotificationsScreen"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationsScreen(
    viewModelDoctor: DoctorViewModel = hiltViewModel(),
    viewModelAppointment: HomeViewModel = hiltViewModel()
) {
    Log.d(TAG, "NotificationsScreen composable started")

    val doctors by viewModelDoctor.doctors.observeAsState(emptyList())
    val appointments by viewModelAppointment.appointments.observeAsState(emptyList())
    val slotsMap by viewModelAppointment.slots.observeAsState(emptyMap())
    val isLoadingAppointments by viewModelAppointment.isLoadingAppointments.observeAsState(false)
    val appointmentsError by viewModelAppointment.appointmentsError.observeAsState(null)

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Предстоящие", "Пропущенные")

    LaunchedEffect(Unit) {
        Log.d(TAG, "Loading doctors and appointments")
        viewModelDoctor.loadDoctors(skip = 0, limit = 5, search = "", serviceId = null, clinicId = null)
        viewModelAppointment.getAppointments()
    }

    val formatter = remember { DateTimeFormatter.ISO_LOCAL_DATE_TIME }
    val now = remember { LocalDateTime.now() }

    // Логируем текущее время для отладки
    Log.d(TAG, "Current time: ${now.format(formatter)}")

    val upcomingAppointments = appointments.filter { appointment ->
        slotsMap[appointment.appointmentSlotId]?.slotTime?.let { slotTimeStr ->
            runCatching {
                Log.d(TAG, "Processing appointment ${appointment.id} with slotTime: $slotTimeStr")
                val slotDateTime = LocalDateTime.parse(slotTimeStr, formatter)
                val isUpcoming = slotDateTime.isAfter(now)
                Log.d(TAG, "Appointment ${appointment.id} parsed time: ${slotDateTime.format(formatter)}, isUpcoming: $isUpcoming")
                isUpcoming
            }.getOrElse { e ->
                Log.e(TAG, "Error parsing time for appointment ${appointment.id}: ${e.message}")
                false
            }
        } ?: run {
            Log.w(TAG, "No slot found for appointment ${appointment.id}")
            false
        }
    }

    val missedAppointments = appointments.filter { appointment ->
        slotsMap[appointment.appointmentSlotId]?.slotTime?.let { slotTimeStr ->
            runCatching {
                Log.d(TAG, "Processing appointment ${appointment.id} with slotTime: $slotTimeStr")
                val slotDateTime = LocalDateTime.parse(slotTimeStr, formatter)
                val isMissed = !slotDateTime.isAfter(now)
                Log.d(TAG, "Appointment ${appointment.id} parsed time: ${slotDateTime.format(formatter)}, isMissed: $isMissed")
                isMissed
            }.getOrElse { e ->
                Log.e(TAG, "Error parsing time for appointment ${appointment.id}: ${e.message}")
                false
            }
        } ?: run {
            Log.w(TAG, "No slot found for appointment ${appointment.id}")
            false
        }
    }

    Log.d(TAG, "Appointments loaded: total=${appointments.size}, upcoming=${upcomingAppointments.size}, missed=${missedAppointments.size}")
    Log.d(TAG, "Doctors loaded: ${doctors.size}")
    Log.d(TAG, "Slots loaded: ${slotsMap.size}")

    // Логируем содержимое slotsMap для отладки
    slotsMap.forEach { (slotId, slot) ->
        Log.d(TAG, "Slot $slotId: ${slot.slotTime}")
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        Log.d(TAG, "Tab selected: $index - ${tabs[index]}")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoadingAppointments -> {
                Log.d(TAG, "Loading appointments...")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            appointmentsError != null -> {
                Log.e(TAG, "Error loading appointments: $appointmentsError")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Ошибка загрузки: $appointmentsError", color = MaterialTheme.colorScheme.error)
                }
            }
            else -> {
                val displayedAppointments = if (selectedTabIndex == 0) upcomingAppointments else missedAppointments

                Log.d(TAG, "Displaying ${displayedAppointments.size} appointments for tab $selectedTabIndex")

                if (displayedAppointments.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (selectedTabIndex == 0) "Нет предстоящих записей" else "Нет пропущенных записей",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(displayedAppointments) { appointment ->
                            val doctor = doctors.find { it.id == appointment.doctorId }
                            val slot = slotsMap[appointment.appointmentSlotId]

                            if (doctor != null && slot != null) {
                                Log.d(TAG, "Rendering RecordCard for appointment ${appointment.id}")
                                Column {
                                    RecordCard(appointment, doctor, slot)

                                    val slotDateTime = runCatching {
                                        LocalDateTime.parse(slot.slotTime, formatter)
                                    }.getOrNull()

                                    val statusLabel = when {
                                        slotDateTime == null -> "Время недоступно"
                                        slotDateTime.isAfter(now) -> "Не пропущено"
                                        else -> "Пропущено"
                                    }

                                    Text(
                                        text = statusLabel,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (statusLabel == "Пропущено") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                                    )
                                }

                            } else {
                                Log.w(TAG, "Missing doctor or slot for appointment ${appointment.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}
