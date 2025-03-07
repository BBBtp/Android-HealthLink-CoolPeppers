package com.CoolPeppers.android.presentation.navigation

import HomeScreen
import ProfileScreen
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
import com.CoolPeppers.android.ui.theme.LightBgSecondary
import com.CoolPeppers.android.ui.theme.LightTextHeaders
import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.presentation.chat.ChatScreen
import com.CoolPeppers.android.presentation.notifications.NotificationsScreen
import com.CoolPeppers.android.presentation.request.RequestScreen
import com.CoolPeppers.android.util.getBottomNavItems

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues
) {

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(paddingValues = padding),

        builder = {
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
                RequestScreen()
            }
            composable("profile") {
                ProfileScreen()
            }
        })
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    NavigationBar(

        containerColor = LightTextHeaders
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val currentRoute = navBackStackEntry?.destination?.route
        val buttomNavItems = getBottomNavItems()
        buttomNavItems.forEach { navItem ->
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