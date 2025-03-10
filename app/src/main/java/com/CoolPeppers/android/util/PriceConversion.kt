package com.CoolPeppers.android.util

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.CoolPeppers.android.ui.theme.LightTextPrimary

@Composable
fun PriceConversion(price: Int) {
    Row {
        repeat(price) {
            Text(
                text = "₽",
                color = LightTextPrimary
            )
        }
    }
}