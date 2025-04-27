package com.CoolPeppers.android.presentation.navigation.bottomNavigation

import ChatScreen
import EditProfile
import HomeScreen
import ProfileScreen
import Settings
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

@OptIn(ExperimentalMaterial3Api::class)
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
                composable("auth") {
                    AuthScreen(navController = navController) // Передаем NavController в AuthScreen
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
                    RequestScreen()
                }
                composable("profile") {
                    ProfileScreen(navController = navController)
                }
                composable("settings") {
                    Settings()
                }
                composable("profile edit") {
                    EditProfile(navController = navController)
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