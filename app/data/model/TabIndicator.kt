package com.CoolPeppers.android.data.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class TabIndicator (
    /**
     * Иконка таб индикатора
     */
    val icon: ImageVector,
    /**
     * Текст таб индикатора
     */
    val label: String,
    /**
     * Экран
     */
    val screen: @Composable () -> Unit
)