import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.CoolPeppers.android.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationMenu() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenu(navController, drawerState)
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("HealthLink") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") { HomeScreen() }
                composable("clinic") { ClinicScreen() }
                composable("doctor") { DoctorScreen() }
                composable("service") { ServiceScreen() }
                composable("logout") { HomeScreen() }
            }
        }
    }
}

@Composable
fun DrawerMenu(navController: NavController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                "Меню",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider()

            NavigationDrawerItem(
                label = {
                    Text(
                        "Клиники",
                    )
                },
                selected = false,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.clinic),
                        contentDescription = null
                    )
                },
                onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate("clinic")
                }
            )

            NavigationDrawerItem(
                label = {
                    Text(
                        "Врачи",
                    )
                },
                selected = false,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.doctor),
                        contentDescription = null
                    )
                },
                onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate("doctor")
                }
            )

            NavigationDrawerItem(
                label = {
                    Text(
                        "Услуги",
                    )
                },
                selected = false,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.service),
                        contentDescription = null
                    )
                },
                onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate("service")
                }
            )

            NavigationDrawerItem(
                label = {
                    Text(
                        "Выйти",
                    )
                },
                selected = false,
                icon = { Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = null) },
                onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate("logout")
                }
            )
        }
    }
}

