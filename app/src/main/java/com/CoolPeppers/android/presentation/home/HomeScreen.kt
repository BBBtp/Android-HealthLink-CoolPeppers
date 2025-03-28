import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.CoolPeppers.android.presentation.doctor.DoctorViewModel
import dagger.hilt.android.AndroidEntryPoint


@Composable
fun HomeScreen(
    viewModel: DoctorViewModel = hiltViewModel() // Внедрение ViewModel через Hilt
) {
    val doctors by viewModel.doctors.observeAsState(emptyList()) // Подписка на LiveData

    LaunchedEffect(Unit) {
        viewModel.loadDoctors(skip = 0, limit = 100, search = "", serviceId = null, clinicId = null)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Главная страница", style = MaterialTheme.typography.headlineLarge)

            if (doctors.isEmpty()) {
                CircularProgressIndicator() // Показываем загрузку
            } else {
                LazyColumn {
                    items(doctors.size) { index ->
                        val doctor = doctors[index]
                        Text(text = doctor.firstName)
                    }
                }
            }
        }
    }
}