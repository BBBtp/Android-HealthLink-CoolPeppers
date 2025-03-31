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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Clinic
import com.CoolPeppers.android.presentation.clinic.ClinicViewModel
import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.ui.theme.Montserrat

@Composable
fun ClinicDetailScreen(clinicId: Int, navController: NavController, viewModelClinic: ClinicViewModel = hiltViewModel()) {

    val clinicById by viewModelClinic.clinic.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModelClinic.loadClinicByID(clinicId = clinicId)
    }


    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    if (clinicById.isEmpty()) {
        ShimmerAnimation()
    } else {
        val clinic: Clinic = clinicById[0]

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
                                model = clinic.logoUrl,
                                contentDescription = "Hospital Image",
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .background(Color.Gray, RoundedCornerShape(14.dp)),
                                contentScale = ContentScale.Crop,
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
                                text = clinic.address,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LightTextPrimary
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.metro),
                                    contentDescription = "Metro Icon",
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = clinic.metro.toString(),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = LightTextPrimary
                                )
                            }
                            Text(
                                text = "${clinic.price}₽",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = LightTextPrimary
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(clinic.rating.toInt()) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = LightTextPrimary
                                    )
                                }
                                repeat(5 - clinic.rating.toInt()) {
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
                            value = "${clinic.customersCount}+",
                            label = "Клиентов",
                            modifier = Modifier.weight(1f)
                        )
                        InfoBox(
                            iconRes = R.drawable.suitcase,
                            value = clinic.yearFoundation.toString(),
                            label = "Год основания",
                            modifier = Modifier.weight(1f)
                        )

                        InfoBox(
                            iconRes = R.drawable.star_clinic,
                            value = clinic.rating.toString(),
                            label = "Рейтинг",
                            modifier = Modifier.weight(1f)
                        )

                        InfoBox(
                            iconRes = R.drawable.message,
                            value = "${clinic.reviewsCount}+",
                            label = "Отзывов",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "О клинике",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = LightTextPrimary
                        )
                        Text(
                            text = clinic.description,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = LightTextPrimary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Время работы",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = LightTextPrimary
                        )
                        Text(
                            text = clinic.workTime.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = LightTextPrimary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            Button(
                onClick =
                {
                    navController.navigate("service/${clinic.id}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEAF4F4),
                    contentColor = LightTextPrimary
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.service),
                    contentDescription = "Service Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Выбрать услугу",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
@Composable
fun InfoBox(iconRes: Int, value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(LightTextPrimary.copy(alpha = 0.08f), CircleShape)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center),
                tint = LightTextPrimary
            )
        }
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = LightTextPrimary,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = LightTextPrimary.copy(alpha = 0.7f)
        )
    }
}