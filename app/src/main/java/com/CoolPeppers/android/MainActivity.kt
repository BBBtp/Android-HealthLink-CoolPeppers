package com.CoolPeppers.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.CoolPeppers.android.ui.theme.AndroidHealthLinkCoolPeppersTheme
import com.CoolPeppers.android.presentation.navigation.BottomNavigationBar
import com.CoolPeppers.android.presentation.navigation.NavHostContainer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidHealthLinkCoolPeppersTheme {
                val navController = rememberNavController()
                Surface(color = Color.White) {
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        }, content = { padding ->
                            NavHostContainer(navController = navController, padding = padding)
                        }
                    )
                }
            }
        }
    }
}
