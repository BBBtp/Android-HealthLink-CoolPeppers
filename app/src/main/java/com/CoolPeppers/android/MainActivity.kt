package com.CoolPeppers.android

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.CoolPeppers.android.data.api.local.TokenManager
import com.CoolPeppers.android.data.repository.AuthRepository
import com.CoolPeppers.android.presentation.navigation.bottomNavigation.BottomNavigationBar
import com.CoolPeppers.android.presentation.navigation.bottomNavigation.NavHostContainer
import com.CoolPeppers.android.ui.theme.AndroidHealthLinkCoolPeppersTheme
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

        Log.d("ContextCheck", "MainActivity context: $this")

        val sharedPreferences = getSharedPreferences("Theme", Context.MODE_PRIVATE)
        val mode = sharedPreferences.getInt("mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        Log.d("themecheck", "theme mode (mainactivity) = $mode")
//        AppCompatDelegate.setDefaultNightMode(mode)

        var darkTheme: Boolean

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            when (mode) {
                AppCompatDelegate.MODE_NIGHT_NO -> darkTheme = false
                AppCompatDelegate.MODE_NIGHT_YES -> darkTheme = true
                else -> darkTheme = isSystemInDarkTheme()
            }
            AndroidHealthLinkCoolPeppersTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                val startDestination = if (isUserAuthenticated()) "home" else "auth"

                Surface {
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
