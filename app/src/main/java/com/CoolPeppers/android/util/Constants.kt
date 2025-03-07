package com.CoolPeppers.android.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.BottomNavItem
import com.CoolPeppers.android.data.model.TabIndicator


@Composable
fun getBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem(
            icon = Icons.Filled.Home,
            route = "home"
        ),
        BottomNavItem(
            icon = ImageVector.vectorResource(id = R.drawable.message),
            route = "chat"
        ),
        BottomNavItem(
            icon = Icons.Filled.Add,
            route = "request"
        ),
        BottomNavItem(
            icon = Icons.Filled.Notifications,
            route = "notifications"
        ),
        BottomNavItem(
            icon = Icons.Filled.Person,
            route = "profile"
        )
    )
}

@Composable
fun getTabIndicatorItems(): List<TabIndicator> {
    return listOf(
       TabIndicator(
           icon = ImageVector.vectorResource(id = R.drawable.clinic),
           label = "Клиники"
       ),
        TabIndicator(
            icon = ImageVector.vectorResource(id = R.drawable.service),
            label = "Услуги"
        ),
        TabIndicator(
            icon = ImageVector.vectorResource(id = R.drawable.doctor),
            label = "Врачи"
        )
    )
}