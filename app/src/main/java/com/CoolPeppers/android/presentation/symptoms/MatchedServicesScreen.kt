package com.CoolPeppers.android.presentation.symptoms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.CoolPeppers.android.R
import com.CoolPeppers.android.presentation.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchedServicesScreen(
    symptoms: List<String>,
    viewModel: SymptomsViewModel = hiltViewModel(),
    navController: NavController
) {
    val matchedServices by viewModel.matchedServices.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState(null)

    LaunchedEffect(symptoms) {
        viewModel.matchServices(symptoms)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.matched_services)) }
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
                        onRetry = { viewModel.matchServices(symptoms) }
                    )
                }
                matchedServices != null -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(matchedServices!!.split("\n").filter { it.isNotBlank() }) { service ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = service,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(
                                    onClick = { navController.navigate("request") },
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    Text(text = stringResource(R.string.book_appointment))
                                }
                            }
                        }
                    }

                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_services_found),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
} 