package com.github.byteguessapplication.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.byteguessapplication.data.local.TipEntity
import com.github.byteguessapplication.presentation.viewmodel.GameViewModel
import kotlinx.coroutines.launch

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    val tips by viewModel.tips.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tips) { tip ->
            val answer by viewModel.getAnswerForCard(tip.cardId).collectAsState("Carregando...")

            TipItem(
                tip = tip,
                answer = answer,
                onCardClick = { viewModel.toggleCardExpansion(tip.cardId) }
            )
        }
    }
}

@Composable
fun TipItem(
    tip: TipEntity,
    answer: String,
    onCardClick: () -> Unit
) {
    var isAnswerVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                isAnswerVisible = !isAnswerVisible
                if (isAnswerVisible) onCardClick()
            }
    ) {
        Text(
            text = tip.text,
            style = MaterialTheme.typography.headlineLarge
        )

        if (isAnswerVisible) {
            Text(
                text = answer,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}