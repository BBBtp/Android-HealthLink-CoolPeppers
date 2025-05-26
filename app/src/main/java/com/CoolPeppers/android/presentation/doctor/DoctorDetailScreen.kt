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
import androidx.compose.ui.res.stringResource
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
//import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.ui.theme.Montserrat
import androidx.compose.runtime.*
import com.CoolPeppers.android.presentation.doctor.DoctorViewModel

@Composable
fun DoctorDetailScreen(
    doctorId: Int, clinicId: Int, serviceId: Int, navController: NavController,
    viewModelDoctor: DoctorViewModel = hiltViewModel()
) {

    val doctorById by viewModelDoctor.doctor.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModelDoctor.loadDoctorByID(doctorId = doctorId)
    }


    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    if (doctorById.isEmpty()) {
        ShimmerAnimation()
    } else {
        val doctor: Doctor = doctorById[0]

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
            ) {
                item {
                    // Header with doctor info
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeight * 0.2f)
                            .background(Color.White, RoundedCornerShape(14.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(150.dp)
                                .height(130.dp)
                                .background(Color.Gray, RoundedCornerShape(14.dp))
                        ) {
                            AsyncImage(
                                model = doctor.photoUrl,
                                contentDescription = stringResource(R.string.doctor_photo),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .background(Color.Gray, RoundedCornerShape(14.dp)),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.doctor_blue)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.6f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = stringResource(R.string.doctor).format(doctor.firstName, doctor.lastName),
                                style = MaterialTheme.typography.titleLarge
                            )

                            doctor.specialization?.let {
                                Text(
                                    text = it,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                   // color = LightTextPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Rating
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(doctor.rating.toInt()) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                    //    tint = LightTextPrimary
                                    )
                                }
                                repeat(5 - doctor.rating.toInt()) {
                                    Icon(
                                        imageVector = Icons.Outlined.Star,
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeight * 0.15f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InfoBox(
                            iconRes = R.drawable.person_clinic,
                            value = "${doctor.customerCount ?: 0}+",
                            label = "Пациентов",
                            modifier = Modifier.weight(1f)
                        )

                        InfoBox(
                            iconRes = R.drawable.suitcase,
                            value = "${doctor.experience ?: 0} лет",
                            label = "Опыт работы",
                            modifier = Modifier.weight(1f)
                        )

                        InfoBox(
                            iconRes = R.drawable.star_clinic,
                            value = doctor.rating.toString(),
                            label = "Рейтинг",
                            modifier = Modifier.weight(1f)
                        )

                        InfoBox(
                            iconRes = R.drawable.message,
                            value = "${doctor.reviewsCount ?: 0}+",
                            label = "Отзывов",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // About doctor section
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.about_doctor),
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = doctor.description ?: "Информация о враче отсутствует",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                           // color = LightTextPrimary,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.working_hours),
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = stringResource(R.string.monday_friday),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }


            Button(
                onClick = { navController.navigate("appointment/${clinicId}/${serviceId}/${doctorId}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEAF4F4),
                    //contentColor = LightTextPrimary
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tooth),
                    contentDescription = stringResource(R.string.schedule_icon),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.schedule),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

