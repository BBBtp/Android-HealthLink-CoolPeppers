import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Service
import androidx. compose. foundation. lazy. grid. LazyVerticalGrid
import androidx. compose. foundation. lazy. grid. GridCells


import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.CoolPeppers.android.presentation.service.ServiceViewModel
import com.CoolPeppers.android.ui.theme.Typography


@Composable
fun ServiceScreen(clinicId: Int, navController: NavController, viewModelService: ServiceViewModel = hiltViewModel()) {
    val services by viewModelService.services.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModelService.loadService(0, 100, "", clinicId = clinicId)
    }


    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    if (services.isEmpty()) {
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
                        .background(Color.White)
                        .padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.service),
                        contentDescription = stringResource(R.string.service_icon),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(R.string.select_service),
                        style = Typography.titleLarge
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.clinic),
                    contentDescription = stringResource(R.string.hospital_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(services) { service ->
                        ServiceItem(service = service, onClick = {
                            navController.navigate("doctor/${clinicId}/${service.id}")
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceItem(service: Service, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color(0xFF2F6690).copy(alpha = 0.08f), shape = RoundedCornerShape(50))
        ) {
            AsyncImage(
                model = service.logoUrl,
                contentDescription = "Clinic Image",
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = service.name,
            color = Color(0xFF2F6690),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
