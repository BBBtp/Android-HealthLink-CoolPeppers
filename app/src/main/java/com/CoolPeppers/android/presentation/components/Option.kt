package com.CoolPeppers.android.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Option(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    onClick: (() -> Unit),
    buttonIcon: ImageVector? = null
) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != {}) Modifier.clickable { onClick() } else Modifier)) {
        Icon(
            imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        if (buttonIcon != null) {
            Icon(
                imageVector = buttonIcon, contentDescription = null, modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OptionPreview() {
    Option(onClick = {}, text = "random", icon = Icons.Default.AccountCircle)
}