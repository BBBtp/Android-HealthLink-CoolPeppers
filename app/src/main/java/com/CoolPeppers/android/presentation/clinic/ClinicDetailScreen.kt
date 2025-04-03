import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.ClinicDetail
import com.CoolPeppers.android.ui.theme.LightTextPrimary
import kotlin.repeat



val clinic = ClinicDetail(
    id = 1,
    name = "Медицинский центр 'Здоровье'",
    city = "Москва",
    metro = "Красные Ворота",
    schedule = "Понедельник - пятница | 9:30 - 18:00",
    info = "Lorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet conseLorem ipsum dolor sit amet consectetur. Eget iaculis est cras ornare augue. Lorem ipsum dolor sit amet consectetur",
    rating = 4,
    price = 5000,
    year = 2000,
    clients = 5000,
    reviews = 5000,
    image = R.drawable.clinic1,
    address = "ул. Киевская 11"
)


@Composable
fun ClinicDetailScreen() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

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
                    Image(
                        painter = painterResource(id = clinic.image),
                        contentDescription = "Hospital Image",
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.4f)
                            .background(Color.Gray, RoundedCornerShape(14.dp))
                    )
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
                                text = clinic.metro,
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
                            repeat(clinic.rating) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = LightTextPrimary
                                )
                            }
                            repeat(5 - clinic.rating) {
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
                        value = "${clinic.clients}+",
                        label = "Клиентов",
                        modifier = Modifier.weight(1f)
                    )
                    InfoBox(
                        iconRes = R.drawable.suitcase,
                        value = clinic.year.toString(),
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
                        value = "${clinic.reviews}+",
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
                        text = clinic.info,
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
                        text = clinic.schedule,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = LightTextPrimary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        Button(
            onClick = {
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