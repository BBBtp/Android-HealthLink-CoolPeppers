import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.ClinicDetail
import com.CoolPeppers.android.ui.theme.LightTextPrimary



val clinic2 = ClinicDetail(
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

val clinicList = List(10) { clinic2 }

@Composable
fun ClinicScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFFffFfFf))
                    .padding(5.dp, 0.dp),
//                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.clinic_blue),
                    contentDescription = "List Icon",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = " Выберите клинику",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(clinicList.size) { index ->
                    HospitalCard(clinic = clinicList[index])
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            
        }
    }
}

@Composable
fun HospitalCard(clinic: ClinicDetail) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color(0xFFEAF4F4), RoundedCornerShape(14.dp))
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(130.dp)
                .background(Color.Gray, RoundedCornerShape(14.dp))
        ) {
            Image(
                painter = painterResource(id = clinic2.image),
                contentDescription = "Hospital Image",
                modifier = Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .width(186.dp)
                .height(130.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = clinic2.address,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = LightTextPrimary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.metro),
                    contentDescription = "Metro Icon",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = clinic2.metro,
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
}