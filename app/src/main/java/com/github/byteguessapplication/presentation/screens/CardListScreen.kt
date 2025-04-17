package com.github.byteguessapplication.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        Button(
            onClick = {
                viewModel.navigateToCreateCard(CardViewModel.CardMode.LIGHT)
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 8.dp)
        ) {
            Text("Criar Novo Card")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* ação do Jogar */ },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 8.dp)
        ) {
            Text("Jogar")
        }



        // Tratar estados
        when (val state = uiState) {
            is CardViewModel.UiState.Loading -> CircularProgressIndicator()
            is CardViewModel.UiState.Error -> Text("Erro: ${state.error}", color = Color.Red)
            is CardViewModel.UiState.CardsLoaded -> {
                // Mostrar cards se necessário
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