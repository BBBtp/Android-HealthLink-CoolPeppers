import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Service
import com.CoolPeppers.android.ui.theme.LightTextPrimary
import androidx. compose. foundation. lazy. grid. LazyVerticalGrid



import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage


val service1 = Service(
    id = 1,
    name = "Стоматология",
    description = "Лечение зубов",
    price = 0,
    duration = 0,
    logoUrl = "https://avatars.mds.yandex.net/get-altay/5449402/2a0000017eaa39ecb506d56384833dca85f5/XXXL"
)
val serviceList = List(10) { service1 }

@Composable
fun ServiceScreen() {
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
                    painter = painterResource(id = R.drawable.service_blue),
                    contentDescription = "List Icon",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = " Выберите услугу",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightTextPrimary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))


            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(serviceList) { service ->
                    ServiceItem(service = service)
                }
            }
        }
    }
}

@Composable
fun ServiceItem(service: Service) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
