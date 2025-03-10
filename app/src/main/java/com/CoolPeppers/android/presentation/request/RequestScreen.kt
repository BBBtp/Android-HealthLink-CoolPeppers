package com.CoolPeppers.android.presentation.request

import ClinicScreen
import DoctorScreen
import ServiceScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.util.getTabIndicatorItems
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RequestScreen(viewModel: RequestViewModel = viewModel()) {
    val tabItems = getTabIndicatorItems()
    var selectedTab by remember { mutableIntStateOf(viewModel.selectedTab) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth(),
            divider = { },
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = LightTextPrimary
                )
            }
        ) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    icon = { Icon(item.icon, contentDescription = null) },
                    text = { Text(text = item.label) },
                    unselectedContentColor = LightTextPrimary,
                    selectedContentColor = LightTextPrimary
                )
            }
        }

        when (selectedTab) {
            0 -> ClinicScreen()
            1 -> ServiceScreen()
            2 -> DoctorScreen()
        }
    }
}
