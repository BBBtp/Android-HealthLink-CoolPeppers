package com.CoolPeppers.android.data.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem (
    /**
     * Иконка таб бара
     */
    val icon: ImageVector,
    /**
     * Маршрут
     */
    val route: String
)