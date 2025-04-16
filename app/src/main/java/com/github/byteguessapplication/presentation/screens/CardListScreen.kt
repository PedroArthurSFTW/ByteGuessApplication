package com.github.byteguessapplication.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.byteguessapplication.presentation.viewmodel.CardViewModel

@Composable
fun CardListScreen(
    viewModel: CardViewModel,
    onNavigate: (CardViewModel.NavigationEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()

    LaunchedEffect(navigationEvent) {
        navigationEvent?.let {
            onNavigate(it)
            viewModel.resetNavigation()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MenuButtonWithDropdown(
            mainText = "Criar Novo Card",
            items = listOf("Light Mode" to CardViewModel.CardMode.LIGHT, "Dark Mode" to CardViewModel.CardMode.DARK),
            onItemSelected = { mode -> viewModel.onCreateCardSelected(mode) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        MenuButtonWithDropdown(
            mainText = "Jogar",
            items = listOf(
                "Light Mode" to CardViewModel.GameMode.LIGHT,
                "Dark Mode" to CardViewModel.GameMode.DARK,
                "Jogar Sozinho" to CardViewModel.GameMode.SOLO
            ),
            onItemSelected = { mode -> viewModel.onPlaySelected(mode) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        MenuButtonWithDropdown(
            mainText = "Editar Card",
            items = listOf("Light Mode" to CardViewModel.CardMode.LIGHT, "Dark Mode" to CardViewModel.CardMode.DARK),
            onItemSelected = { mode -> viewModel.onEditSelected(mode) }
        )

        when (val state = uiState) {
            is CardViewModel.UiState.Loading -> CircularProgressIndicator()
            is CardViewModel.UiState.Error -> Text("Erro: ${state.error}", color = Color.Red)
            is CardViewModel.UiState.CardsLoaded -> {

            }
            else -> {}
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