import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.*
import com.CoolPeppers.android.presentation.appointment.AppointmentViewModel
import com.CoolPeppers.android.presentation.clinic.ClinicViewModel
import com.CoolPeppers.android.presentation.doctor.DoctorViewModel
import com.CoolPeppers.android.presentation.service.ServiceViewModel
import com.CoolPeppers.android.ui.theme.Typography
import kotlinx.coroutines.launch

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext

import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PaymentScreen(
    clinicId: Int,
    serviceId: Int,
    doctorId: Int,
    slotId: Int,
    navController: NavController,
    viewModelClinic: ClinicViewModel = hiltViewModel(),
    viewModelDoctor: DoctorViewModel = hiltViewModel(),
    viewModelService: ServiceViewModel = hiltViewModel(),
    viewModelAppointment: AppointmentViewModel = hiltViewModel(),
) {
    val clinicById by viewModelClinic.clinic.observeAsState(emptyList())
    val doctorById by viewModelDoctor.doctor.observeAsState(emptyList())
    val serviceById by viewModelService.service.observeAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val selectedCard = remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModelService.loadServiceByID(serviceId = serviceId)
        viewModelClinic.loadClinicByID(clinicId = clinicId)
        viewModelDoctor.loadDoctorByID(doctorId = doctorId)
    }

    if ( clinicById.isEmpty() || doctorById.isEmpty() || serviceById.isEmpty() ) {
        ShimmerAnimation()
    } else {
        val service: Service = serviceById[0]
        val clinic: Clinic = clinicById[0]
        val doctor: Doctor = doctorById[0]

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 96.dp)
            ) {
                item {
                    AppointmentInfoCard(
                        serviceName = service.name,
                        serviceDescription = service.description ?: "",
                        doctorName = "${doctor.firstName} ${doctor.lastName}",
                        clinicName = clinic.name,
                        price = service.price ?: 0
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = stringResource(R.string.your_cards),
                        style = Typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFEAF4F4)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            PaymentCard(
                                isSelected = selectedCard.value == 0,
                                onClick = { selectedCard.value = 0 },
                                iconRes = R.drawable.mastercard,
                                bankName = "Axis Bank",
                                cardNumber = "**** **** **** 8395"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            PaymentCard(
                                isSelected = selectedCard.value == 1,
                                onClick = { selectedCard.value = 1 },
                                iconRes = R.drawable.visa,
                                bankName = "HDFC Bank",
                                cardNumber = "**** **** **** 6246"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            AddNewCardButton()
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OrDivider()

                    Spacer(modifier = Modifier.height(20.dp))

                    PaymentSystemsList()
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(Color(0xFFEAF4F4))
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.currency_symbol).format(service.price),
                        style = Typography.titleLarge
                    )
                    Text(
                        text = stringResource(R.string.details),
                        style = Typography.bodyMedium
                    )
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                slotId?.let { slot ->
                                    viewModelAppointment.createAppointment(
                                        clinicId = clinicId,
                                        serviceId = serviceId,
                                        doctorId = doctorId,
                                        slotId = slot
                                    )
                                    Toast.makeText(
                                        context,
                                        "Вы успешно записаны!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.navigate("paymentsuccess")
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Ошибка при записи: ${e.localizedMessage}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .width(214.dp)
                        .height(49.dp),
                    shape = RoundedCornerShape(42.dp),
                    colors = ButtonDefaults.buttonColors(
                    )
                ) {
                    Text(
                        text = stringResource(R.string.pay),
                        style = Typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun AppointmentInfoCard(
    serviceName: String,
    serviceDescription: String,
    doctorName: String,
    clinicName: String,
    price: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEAF4F4)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.service),
                        contentDescription = stringResource(R.string.service_icon),
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f).padding(start = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.appointment),
                        style = Typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = serviceName,
                        style = Typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.doctor).format(doctorName),
                        style = Typography.bodyMedium
                    )
                    Text(
                        text = stringResource(R.string.clinic).format(clinicName),
                        style = Typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(11.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.total),
                    style = Typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.price_format, price),
                    style = Typography.titleLarge
                )
                Text(
                    text = stringResource(R.string.currency_symbol).format(price),
                    style = Typography.titleLarge
                )
            }
        }
    }
}

@Composable
private fun PaymentCard(
    isSelected: Boolean,
    onClick: () -> Unit,
    iconRes: Int,
    bankName: String,
    cardNumber: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = stringResource(R.string.card),
                    modifier = Modifier.size(32.dp, 20.dp)
                )
                Column {
                    Text(
                        text = bankName,
                        style = Typography.bodyMedium
                    )
                    Text(
                        text = cardNumber,
                        style = Typography.bodyMedium
                    )
                }
            }

            Box(modifier = Modifier.size(20.dp)) {
                if (isSelected) {
                    Icon(
                        painter = painterResource(id = R.drawable.selected_card),
                        contentDescription = stringResource(R.string.selected),
                        tint = Color.Green
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.unelected_card),
                        contentDescription = stringResource(R.string.not_selected),
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun AddNewCardButton() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle add new card */ },
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.add_new_card),
                style = Typography.bodyMedium
            )
        }
    }
}

@Composable
private fun OrDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(32.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            thickness = 1.dp
        )
        Box(
            modifier = Modifier
                .size(40.dp, 32.dp)
                .align(Alignment.Center)
                .background(Color.White)
        )
        Text(
            text = stringResource(R.string.or_payment),
            style = Typography.bodyMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun PaymentSystemsList() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PaymentSystemButton(
            iconRes = R.drawable.sber,
            text = stringResource(R.string.sber_pay),
            backgroundColor = Color(0xFF21A038)
        )

        PaymentSystemButton(
            iconRes = R.drawable.vtb,
            text = stringResource(R.string.vtb),
            backgroundColor = Color(0xFF009FDF)
        )

        PaymentSystemButton(
            iconRes = R.drawable.vk_pay,
            text = stringResource(R.string.vk_pay),
            backgroundColor = Color(0xFF0077FF)
        )
    }
}

@Composable
private fun PaymentSystemButton(
    iconRes: Int,
    text: String,
    backgroundColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { /* Handle payment system selection */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                modifier = Modifier.size(240.dp, 60.dp)
            )
        }
    }
}



