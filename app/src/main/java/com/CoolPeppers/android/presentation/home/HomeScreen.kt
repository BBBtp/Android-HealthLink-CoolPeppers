import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.CoolPeppers.android.data.model.Appointment
import com.CoolPeppers.android.presentation.clinic.ClinicViewModel
import com.CoolPeppers.android.presentation.doctor.DoctorViewModel
import com.CoolPeppers.android.presentation.home.HomeViewModel
import com.CoolPeppers.android.presentation.home.components.ClinicCard
import com.CoolPeppers.android.presentation.home.components.DoctorCard
import com.CoolPeppers.android.presentation.home.components.RecordCard
import com.CoolPeppers.android.presentation.home.components.UserInfoBlock
import com.CoolPeppers.android.presentation.profile.ProfileViewModel
import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.ui.theme.Montserrat


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

    LaunchedEffect(Unit) {
        viewModelDoctor.loadDoctors(skip = 0, limit = 100, search = "", serviceId = null, clinicId = null)
        viewModelClinic.loadClinics(skip = 0, limit = 100, search = "")
        viewModelAppointment.getAppointments()
    }
    if (doctors.isEmpty() || clinics.isEmpty() || appointments.isEmpty()) {
        ShimmerAnimation()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp)
        ) {
            UserInfoBlock(user, navController)
            Text(
                text = "Записи",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = LightTextPrimary
                ),
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(appointments) { appointment ->
                    val doctor = doctorsMap[appointment.doctorId]
                    val slot = slotsMap[appointment.appointmentSlotId]

                    if (doctor != null && slot != null) {
                        RecordCard(appointment,doctor,slot)
                    }
                }
            }
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = "Врачи",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = LightTextPrimary
                ),
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(doctors) { doctor ->
                    DoctorCard(doctor)
                }
            }
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = "Поликлиники",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = LightTextPrimary
                ),
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(clinics) { clinic ->
                    ClinicCard(clinic)
                }
            }
        }
    }
}