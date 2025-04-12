package com.github.byteguessapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.github.byteguessapplication.data.CardRepository

class CardViewModel(private val repository: CardRepository) : ViewModel() {
    val allCards = repository.allCards
    //TODO:métodos CRUD
}