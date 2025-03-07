package com.CoolPeppers.android.data.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    /**
     * Иконка
     */
    val icon: ImageVector,
    /**
     * Роут на экран
     */
    val route:String,
)