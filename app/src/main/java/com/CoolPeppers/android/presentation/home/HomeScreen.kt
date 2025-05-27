import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Appointment
import com.CoolPeppers.android.presentation.clinic.ClinicViewModel
import com.CoolPeppers.android.presentation.doctor.DoctorViewModel
import com.CoolPeppers.android.presentation.home.HomeViewModel
import com.CoolPeppers.android.presentation.home.components.ClinicCard
import com.CoolPeppers.android.presentation.home.components.DoctorCard
import com.CoolPeppers.android.presentation.home.components.RecordCard
import com.CoolPeppers.android.presentation.profile.ProfileViewModel
//import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.ui.theme.Montserrat
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    viewModelDoctor: DoctorViewModel = hiltViewModel(),
    viewModelClinic: ClinicViewModel = hiltViewModel(),
    viewModelAppointment: HomeViewModel = hiltViewModel(),
    viewModelProfile: ProfileViewModel = hiltViewModel(),
    navController: NavController
) {
    val doctors by viewModelDoctor.doctors.observeAsState(emptyList())
    val clinics by viewModelClinic.clinics.observeAsState(emptyList())
    val appointments by viewModelAppointment.appointments.observeAsState(emptyList())
    val doctorsMap by viewModelAppointment.doctors.observeAsState(emptyMap())
    val slotsMap by viewModelAppointment.slots.observeAsState(emptyMap())
    val user by viewModelProfile.userState.collectAsState()

    // Состояния загрузки
    val isLoadingAppointments by viewModelAppointment.isLoadingAppointments.observeAsState(false)
    val isLoadingDoctors by viewModelAppointment.isLoadingDoctors.observeAsState(false)
    val isLoadingSlots by viewModelAppointment.isLoadingSlots.observeAsState(false)


    // Состояния ошибок
    val appointmentsError by viewModelAppointment.appointmentsError.observeAsState(null)
    val doctorsError by viewModelAppointment.doctorsError.observeAsState(null)
    val slotsError by viewModelAppointment.slotsError.observeAsState(null)


    // Эффект для начальной загрузки данных
    LaunchedEffect(Unit) {
        launch {
            viewModelDoctor.loadDoctors(skip = 0, limit = 5, search = "", serviceId = null, clinicId = null)
        }

        launch {
            viewModelClinic.loadClinics(skip = 0, limit = 5, search = "")
        }

        launch {
            viewModelAppointment.getAppointments()
        }
    }

    val doctorIdsInAppointments = appointments.map { it.doctorId }.toSet()
    val filteredDoctors = doctors.filter { it.id in doctorIdsInAppointments }

    val clinicIdsInAppointments = appointments.map { it.clinicId }.toSet()
    val filteredClinics = clinics.filter { it.id in clinicIdsInAppointments }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(26.dp)
    ) {

        // Секция записей
        Text(
            text = stringResource(R.string.records),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        when {
            isLoadingAppointments -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            appointmentsError != null -> {
                ErrorView(
                    message = appointmentsError!!,
                    onRetry = { viewModelAppointment.retryLoading() }
                )
            }
            appointments.isNotEmpty() -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(appointments) { appointment ->
                        val doctor = doctorsMap[appointment.doctorId]
                        val slot = slotsMap[appointment.appointmentSlotId]

                        if (doctor != null && slot != null) {
                            RecordCard(appointment, doctor, slot)
                        }
                    }
                }
            }
            else -> {
                EmptyStateView(message = stringResource(R.string.no_records))
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Секция врачей
        Text(
            text = stringResource(R.string.doctors),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        when {
            isLoadingDoctors -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            doctorsError != null -> {
                ErrorView(
                    message = doctorsError!!,
                    onRetry = { viewModelDoctor.loadDoctors(0, 5, "", null, null) }
                )
            }
            doctors.isNotEmpty() -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredDoctors) { doctor ->
                        DoctorCard(
                            doctor = doctor,
                            onClick = {
//                                navController.navigate("doctor_detail/${null}/${null}/${doctor.id}")
                            }
                        )
                    }
                }
            }
            else -> {
                EmptyStateView(message = stringResource(R.string.no_doctors))
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Секция клиник
        Text(
            text = stringResource(R.string.clinics),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        when {
            clinics.isEmpty()-> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            clinics.isNotEmpty() -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredClinics) { clinic ->
                        ClinicCard(
                            clinic = clinic,
                            onClick = {
                                navController.navigate("clinic_detail/${clinic.id}")
                            }
                        )
                    }
                }
            }
            else -> {
                EmptyStateView(message = stringResource(R.string.no_clinics))
            }
        }
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = Color.Red,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Повторить")
        }
    }
}

@Composable
fun EmptyStateView(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}