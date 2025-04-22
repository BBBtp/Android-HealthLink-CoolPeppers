import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Clinic
import com.CoolPeppers.android.data.model.Doctor
import com.CoolPeppers.android.presentation.clinic.ClinicViewModel
import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.ui.theme.Montserrat
import androidx.compose.runtime.*
import com.CoolPeppers.android.presentation.appointment.AppointmentViewModel
import com.CoolPeppers.android.presentation.doctor.DoctorViewModel
import kotlinx.coroutines.launch


import android.widget.Toast

import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.platform.LocalContext



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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                val randomSlotId = (1..10).random()

                viewModel.createAppointment(
                    clinicId = clinicId,
                    serviceId = serviceId,
                    doctorId = doctorId,
                    slotId = randomSlotId
                )


                coroutineScope.launch {
                    Toast.makeText(context, "Вы записаны! Слот №$randomSlotId", Toast.LENGTH_LONG).show()
                }
            },
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEAF4F4),
                contentColor = Color(0xFF1A1A1A)
            )
        ) {
            Text(
                text = "Записаться",
                fontSize = 18.sp
            )
        }
    }
}
