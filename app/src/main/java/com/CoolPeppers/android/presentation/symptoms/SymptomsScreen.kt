package com.CoolPeppers.android.presentation.symptoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Symptom
import com.CoolPeppers.android.presentation.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomsScreen(
    viewModel: SymptomsViewModel = hiltViewModel(),
    navController: NavController
) {
    val symptoms by viewModel.symptoms.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState(null)
    var selectedSymptoms by remember { mutableStateOf(emptyList<String>()) }

    LaunchedEffect(Unit) {
        viewModel.loadSymptoms()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.select_symptoms)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    ErrorView(
                        message = error!!,
                        onRetry = { viewModel.loadSymptoms() }
                    )
                }
                symptoms.isNullOrEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_symptoms),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(symptoms!!) { symptom ->
                            SymptomChip(
                                symptom = symptom,
                                isSelected = selectedSymptoms.contains(symptom.name),
                                onSelect = { isSelected ->
                                    selectedSymptoms = if (isSelected) {
                                        selectedSymptoms + symptom.name
                                    } else {
                                        selectedSymptoms - symptom.name
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (selectedSymptoms.isNotEmpty()) {
                                navController.navigate("matched_services/${selectedSymptoms.joinToString(",")}")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = selectedSymptoms.isNotEmpty()
                    ) {
                        Text(text = stringResource(R.string.find_services))
                    }
                }
            }
        }
    }
}

@Composable
fun SymptomChip(
    symptom: Symptom,
    isSelected: Boolean,
    onSelect: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(!isSelected) },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        border = if (!isSelected) {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        } else null

    ) {
        Text(
            text = symptom.name,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
} 