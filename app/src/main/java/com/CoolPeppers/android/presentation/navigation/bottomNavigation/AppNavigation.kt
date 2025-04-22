package com.CoolPeppers.android.presentation.navigation.bottomNavigation

import ChatScreen
import ClinicDetailScreen
import DoctorDetailScreen
import DoctorScreen
import HomeScreen
import ProfileScreen
import ServiceScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.CoolPeppers.android.presentation.authentication.AuthScreen
import com.CoolPeppers.android.presentation.chat.LocalBottomBarVisibility
import com.CoolPeppers.android.ui.theme.LightBgSecondary
import com.CoolPeppers.android.ui.theme.LightTextHeaders
import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.presentation.notifications.NotificationsScreen
import com.CoolPeppers.android.presentation.request.RequestScreen
import com.CoolPeppers.android.util.getBottomNavItems

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
                composable("auth") {
                    AuthScreen(navController = navController) // Передаем NavController в AuthScreen
                }

                composable("home") {
                    HomeScreen()
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
                composable("clinic_detail/{clinicId}") { backStackEntry ->
                    val clinicId = backStackEntry.arguments?.getString("clinicId")?.toIntOrNull()
                    clinicId?.let {
                        ClinicDetailScreen(clinicId = it,navController = navController)
                    }
                }
                composable("service/{clinicId}") { backStackEntry ->
                    val clinicId = backStackEntry.arguments?.getString("clinicId")?.toIntOrNull()
                    clinicId?.let {
                        ServiceScreen(clinicId = it,navController = navController)
                    }
                }
                composable("doctor/{clinicId}/{serviceId}") { backStackEntry ->
                    val clinicId = backStackEntry.arguments?.getString("clinicId")?.toIntOrNull()
                    val serviceId = backStackEntry.arguments?.getString("serviceId")?.toIntOrNull()

                    if (clinicId != null && serviceId != null) {
                        DoctorScreen(clinicId = clinicId, serviceId = serviceId, navController = navController)
                    }
                }
                composable("doctor_detail/{doctorId}") { backStackEntry ->
                    val doctorId = backStackEntry.arguments?.getString("doctorId")?.toIntOrNull()
                    doctorId?.let {
                        DoctorDetailScreen(doctorId = it,navController = navController)
                    }
                }
            })
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val bottomBarVisibility = LocalBottomBarVisibility.current

    if (!bottomBarVisibility.value) return

    NavigationBar(
        containerColor = LightTextHeaders
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val bottomNavItems = getBottomNavItems()

        bottomNavItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route)
                },
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = null)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LightTextPrimary,
                    unselectedIconColor = LightTextPrimary,
                    selectedTextColor = LightTextPrimary,
                    indicatorColor = LightBgSecondary
                )
            )
        }
    }
}