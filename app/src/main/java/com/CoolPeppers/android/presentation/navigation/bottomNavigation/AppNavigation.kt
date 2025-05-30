package com.CoolPeppers.android.presentation.navigation.bottomNavigation

import AppointmentScreen
import ChatScreen
import ClinicDetailScreen
import DoctorDetailScreen
import DoctorScreen
import EditProfile
import HomeScreen
import PaymentScreen
import com.CoolPeppers.android.presentation.settings.Settings
import ProfileScreen
import ServiceScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.CoolPeppers.android.presentation.appointment.AppointmentDetail
import com.CoolPeppers.android.presentation.authentication.AuthScreen

import com.CoolPeppers.android.presentation.notifications.NotificationsScreen
import com.CoolPeppers.android.presentation.payment.PaymentSuccessScreen
import com.CoolPeppers.android.presentation.request.RequestScreen
import com.CoolPeppers.android.presentation.symptoms.MatchedServicesScreen
import com.CoolPeppers.android.presentation.symptoms.SymptomsScreen
import com.CoolPeppers.android.ui.theme.LocalBottomBarVisibility
import com.CoolPeppers.android.util.getBottomNavItems

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(paddingValues = padding),
        builder = {
            // Основные экраны
            composable("auth") {
                AuthScreen(navController = navController)
            }
            composable("home") {
                HomeScreen(navController = navController)
            }
            composable("chat") {
                ChatScreen()
            }
            composable("notifications") {
                NotificationsScreen()
            }
            composable("request") {
                RequestScreen(navController = navController)
            }
            composable("profile") {
                ProfileScreen(navController = navController)
            }
            composable("settings") {
                Settings(modifier = Modifier.padding(30.dp))
            }
            composable("profile edit") {
                EditProfile(navController = navController)
            }

            // Экраны клиник и записей
            composable("clinic_detail/{clinicId}") { backStackEntry ->
                val clinicId = backStackEntry.arguments?.getString("clinicId")?.toIntOrNull()
                clinicId?.let {
                    ClinicDetailScreen(clinicId = it, navController = navController)
                }
            }
            composable("service/{clinicId}") { backStackEntry ->
                val clinicId = backStackEntry.arguments?.getString("clinicId")?.toIntOrNull()
                clinicId?.let {
                    ServiceScreen(clinicId = it, navController = navController)
                }
            }
            composable("doctor/{clinicId}/{serviceId}") { backStackEntry ->
                val clinicId = backStackEntry.arguments?.getString("clinicId")?.toIntOrNull()
                val serviceId = backStackEntry.arguments?.getString("serviceId")?.toIntOrNull()

                if (clinicId != null && serviceId != null) {
                    DoctorScreen(
                        clinicId = clinicId,
                        serviceId = serviceId,
                        navController = navController
                    )
                }
            }
            composable("doctor_detail/{clinicId}/{serviceId}/{doctorId}") { backStackEntry ->
                val clinicId = backStackEntry.arguments?.getString("clinicId")?.toIntOrNull()
                val serviceId = backStackEntry.arguments?.getString("serviceId")?.toIntOrNull()
                val doctorId = backStackEntry.arguments?.getString("doctorId")?.toIntOrNull()
                if (clinicId != null && serviceId != null && doctorId != null) {
                    DoctorDetailScreen(
                        clinicId = clinicId,
                        serviceId = serviceId,
                        doctorId = doctorId,
                        navController = navController
                    )
                }
            }
            composable("appointment/{clinicId}/{serviceId}/{doctorId}") { backStackEntry ->
                val clinicId = backStackEntry.arguments?.getString("clinicId")?.toIntOrNull()
                val serviceId = backStackEntry.arguments?.getString("serviceId")?.toIntOrNull()
                val doctorId = backStackEntry.arguments?.getString("doctorId")?.toIntOrNull()

                if (clinicId != null && serviceId != null && doctorId != null) {
                    AppointmentScreen(
                        clinicId = clinicId,
                        serviceId = serviceId,
                        doctorId = doctorId,
                        navController = navController
                    )
                }
            }
            composable("appointment_detail/{appointmentId}") { backStackEntry ->
                val appointmentId = backStackEntry.arguments?.getString("appointmentId")?.toIntOrNull()

                if (appointmentId != null) {
                    AppointmentDetail(
                        appointmentId = appointmentId,
                        navController = navController
                    )
                }
            }
            composable("payment/{clinicId}/{serviceId}/{doctorId}/{slotId}") { backStackEntry ->
                val clinicId = backStackEntry.arguments?.getString("clinicId")?.toIntOrNull()
                val serviceId = backStackEntry.arguments?.getString("serviceId")?.toIntOrNull()
                val doctorId = backStackEntry.arguments?.getString("doctorId")?.toIntOrNull()
                val slotId = backStackEntry.arguments?.getString("slotId")?.toIntOrNull()

                if (clinicId != null && serviceId != null && doctorId != null && slotId != null) {
                    PaymentScreen(
                        clinicId = clinicId,
                        serviceId = serviceId,
                        doctorId = doctorId,
                        slotId = slotId,
                        navController = navController,
                    )
                }
            }
            composable("paymentsuccess") {
                PaymentSuccessScreen(navController = navController)
            }

            // Маршруты для симптомов
            composable("symptoms") {
                SymptomsScreen(navController = navController)
            }

            composable("matched_services/{symptoms}") { backStackEntry ->
                val symptoms = backStackEntry.arguments?.getString("symptoms")?.split(",") ?: emptyList()
                MatchedServicesScreen(symptoms = symptoms, navController = navController)
            }
        }
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val bottomBarVisibility = LocalBottomBarVisibility.current

    if (!bottomBarVisibility.value) return

    NavigationBar(
        //containerColor = LightTextHeaders
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val bottomNavItems = getBottomNavItems()

        bottomNavItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        // Очистка стека навигации при переходе на основные экраны
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = null)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = LightTextPrimary,
//                    unselectedIconColor = LightTextPrimary,
//                    selectedTextColor = LightTextPrimary,
//                    indicatorColor = LightBgSecondary
                )
            )
        }
    }
}