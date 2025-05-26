package com.CoolPeppers.android.util

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.CoolPeppers.android.R
//import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.ui.theme.Typography

@Composable
fun PriceConversion(price: Int) {
    Row {
        repeat(price) {
            Text(
                text = stringResource(R.string.currency_symbol),
                style = Typography.bodyMedium
            )
        }
    }
}