import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Clinic
import com.CoolPeppers.android.data.model.ClinicDetail
import com.CoolPeppers.android.presentation.clinic.ClinicViewModel
//import com.CoolPeppers.android.ui.theme.LightTextPrimary
//import com.CoolPeppers.android.ui.theme.Montserrat
import com.CoolPeppers.android.util.PriceConversion

@Composable
fun ClinicScreen(navController: NavController, viewModelClinic: ClinicViewModel = hiltViewModel()) {
    val clinics by viewModelClinic.clinics.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModelClinic.loadClinics(skip = 0, limit = 100, search = "")
    }

    if (clinics.isEmpty()) {
        ShimmerAnimation()
    } else {
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.clinic_blue),
                        contentDescription = "List Icon",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(R.string.select_your_clinic),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                       // color = LightTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(clinics.size) { index ->
                        HospitalCard(clinic = clinics[index], onClick = {
                            navController.navigate("clinic_detail/${clinics[index].id}")
                        })
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                }
            }
        }
    }
}

@Composable
fun HospitalCard(clinic: Clinic, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color(0xFFEAF4F4), RoundedCornerShape(14.dp))
            .padding(10.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
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

        Column(
            modifier = Modifier
                .width(186.dp)
                .height(130.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = clinic.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
              //  color = LightTextPrimary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.metro),
                    contentDescription = "Metro Icon",
                    modifier = Modifier.size(20.dp)
                )
                clinic.metro?.let {
                    Text(
                        text = it,
                        style = androidx.compose.ui.text.TextStyle(
                           // fontFamily = Montserrat,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp,
                           // color = LightTextPrimary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Text(
                text = clinic.address,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                //color = LightTextPrimary
            )
            Text(
                text = "${clinic.price}₽",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
               // color = LightTextPrimary
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
                        //tint = LightTextPrimary
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
}