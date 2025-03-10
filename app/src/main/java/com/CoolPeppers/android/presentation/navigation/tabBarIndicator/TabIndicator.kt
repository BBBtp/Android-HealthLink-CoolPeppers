package com.CoolPeppers.android.presentation.navigation.tabBarIndicator

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class TabIndicator(
    /**
     * Иконка таб индикатора
     */
    val icon: ImageVector,
    /**
     * Текст под иконкой
     */
    val label: String
)