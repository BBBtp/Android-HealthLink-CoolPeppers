package com.CoolPeppers.android

import TopNavigationMenu
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.CoolPeppers.android.ui.theme.AndroidHealthLinkCoolPeppersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidHealthLinkCoolPeppersTheme {
                TopNavigationMenu()
            }
        }
    }
}
