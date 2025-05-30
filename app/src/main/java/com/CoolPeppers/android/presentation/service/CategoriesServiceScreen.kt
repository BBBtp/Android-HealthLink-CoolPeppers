import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Service
//import com.CoolPeppers.android.ui.theme.LightTextPrimary
import androidx. compose. foundation. lazy. grid. LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells



import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.CoolPeppers.android.presentation.service.ServiceViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesServiceScreen(navController: NavController, viewModelService: ServiceViewModel = hiltViewModel())  {
    val services by viewModelService.services.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModelService.loadService(0, 100, "", clinicId = null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
//                            .background(Color.White)
                            .padding(horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.service),
                            contentDescription = stringResource(R.string.service_icon),
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = stringResource(R.string.select_service),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("symptoms") }) {
                        Icon(
                            painter = painterResource(R.drawable.robot),
                            contentDescription = stringResource(R.string.ai_assistant),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))


            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(services) { service ->
                    ServiceItem(service = service, onClick = {
                        service.clinicId?.let { clinicId ->
                            navController.navigate("doctor/${clinicId}/${service.id}")
                        }
                    })
                }
            }
        }
    }
}