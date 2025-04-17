package com.github.byteguessapplication.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.byteguessapplication.presentation.viewmodel.CardViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CardListScreen(
    viewModel: CardViewModel,
    onNavigate: (CardViewModel.NavigationEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            event?.let {
                onNavigate(it)
                viewModel.resetNavigation()
            }
        }
    }

    Scaffold { paddingValues -> // Use the padding provided by Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply Scaffold padding
                .padding(16.dp), // Apply your screen-specific padding
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    viewModel.requestCreateCard()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Criar Novo Card")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.requestPlay()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Jogar")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.requestEditCard()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar Card")
            }

            Spacer(modifier = Modifier.height(32.dp))

            when (val state = uiState) {
                is CardViewModel.UiState.Loading -> CircularProgressIndicator()
                is CardViewModel.UiState.Error -> Text(
                    text = "Erro: ${state.error}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                else -> {}
            }
        }
    }
}

@Composable
private fun <T> MenuButtonWithDropdown(
    mainText: String,
    items: List<Pair<String, T>>,
    onItemSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(mainText)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { (text, mode) ->
                DropdownMenuItem(
                    text = { Text(text) },
                    onClick = {
                        onItemSelected(mode)
                        expanded = false
                    }
                )
            }
        }
    }
}