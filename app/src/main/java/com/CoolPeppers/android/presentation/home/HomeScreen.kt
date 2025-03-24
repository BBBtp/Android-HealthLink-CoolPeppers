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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Clinic
import com.CoolPeppers.android.presentation.clinic.ClinicViewModel
import com.CoolPeppers.android.presentation.doctor.DoctorViewModel
import com.CoolPeppers.android.presentation.home.components.ClinicCard
import com.CoolPeppers.android.presentation.home.components.DoctorCard
import com.CoolPeppers.android.presentation.home.components.RecordCard
import com.CoolPeppers.android.presentation.home.components.UserInfoBlock
import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.ui.theme.Montserrat
import dagger.hilt.android.AndroidEntryPoint


@Composable
fun HomeScreen(
    viewModelDoctor: DoctorViewModel = hiltViewModel(),
    viewModelClinic: ClinicViewModel = hiltViewModel()
) {
    val doctors by viewModelDoctor.doctors.observeAsState(emptyList())
    val clinics by viewModelClinic.clinics.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModelDoctor.loadDoctors(skip = 0, limit = 100, search = "", serviceId = null, clinicId = null)
        viewModelClinic.loadClinics(skip = 0, limit = 100, search = "")
    }
    if (doctors.isEmpty() || clinics.isEmpty()) {
        ShimmerAnimation()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp)
                .verticalScroll(rememberScrollState())
        ) {

            UserInfoBlock()
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = "Ваши врачи",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = LightTextPrimary
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                doctors.map { doctor ->
                    DoctorCard(doctor)
                }
            }
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = "Ваши поликлиники",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = LightTextPrimary
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                clinics.map { clinic ->
                    ClinicCard(clinic)
                }
            }
        }
    }
}