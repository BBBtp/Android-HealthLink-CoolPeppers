package com.CoolPeppers.android

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.CoolPeppers.android.data.api.local.TokenManager
import com.CoolPeppers.android.data.model.AuthResponse
import com.CoolPeppers.android.data.repository.AuthRepository
import com.CoolPeppers.android.presentation.authentication.AuthScreen
import com.CoolPeppers.android.ui.theme.AndroidHealthLinkCoolPeppersTheme
import com.CoolPeppers.android.presentation.navigation.bottomNavigation.BottomNavigationBar
import com.CoolPeppers.android.presentation.navigation.bottomNavigation.NavHostContainer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenManager: TokenManager
    @Inject
    lateinit var authRepository: AuthRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndroidHealthLinkCoolPeppersTheme {
                val navController = rememberNavController()
                val startDestination = if (isUserAuthenticated()) "home" else "auth"

                Surface(color = Color.White) {
                    Scaffold(
                        bottomBar = {
                            val currentBackStackEntry = navController.currentBackStackEntryAsState().value
                            val currentRoute = currentBackStackEntry?.destination?.route
                            if (currentRoute != "auth") { // Прячем BottomNavigationBar только на экране авторизации
                                BottomNavigationBar(navController = navController)
                            }
                        },
                        content = { padding ->
                            NavHostContainer(
                                navController = navController,
                                padding = padding,
                                startDestination = startDestination // Передаем начальный экран
                            )
                        }
                    )
                }
            }
        }
    }

    private fun isUserAuthenticated(): Boolean {
        val token = tokenManager.getAccessToken()
       return token!=null
    }

}
