package com.github.byteguessapplication.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.github.byteguessapplication.presentation.viewmodel.CardViewModel

@Composable
fun CardListScreen(viewModel: CardViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Quem ler eh gay",
            color = Color.Red, // Cor contrastante
            fontSize = 30.sp
        )
    }
    //TODO:implementação da tela
}