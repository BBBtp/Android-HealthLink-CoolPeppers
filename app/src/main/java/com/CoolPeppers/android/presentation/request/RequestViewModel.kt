package com.CoolPeppers.android.presentation.request

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RequestViewModel : ViewModel() {
    var selectedTab by mutableIntStateOf(0)
        private set

    fun selectTab(index: Int) {
        selectedTab = index
    }

}
