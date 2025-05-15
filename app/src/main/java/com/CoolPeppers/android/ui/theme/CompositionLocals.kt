package com.CoolPeppers.android.ui.theme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

val LocalBottomBarVisibility = staticCompositionLocalOf { mutableStateOf(true) }