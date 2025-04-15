package com.github.byteguessapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.byteguessapplication.data.local.CardRepository
import com.github.byteguessapplication.data.local.CardEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardViewModel(private val repository: CardRepository) : ViewModel() {

    // Estados da UI
    sealed class UiState {
        object Loading : UiState()
        data class Success(val message: String? = null) : UiState()
        data class Error(val error: String) : UiState()
        data class CardsLoaded(val cards: List<CardEntity>) : UiState()
    }

    sealed class NavigationEvent {
        data class NavigateToCreateCard(val mode: CardMode) : NavigationEvent()
        data class NavigateToPlay(val mode: GameMode) : NavigationEvent()
        data class NavigateToEdit(val mode: CardMode) : NavigationEvent()
    }

    enum class CardMode { LIGHT, DARK }
    enum class GameMode { LIGHT, DARK, SOLO }

    private val _uiState = MutableStateFlow<UiState>(UiState.Success())
    val uiState: StateFlow<UiState> = _uiState

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent

    // Funções chamadas pela UI
    fun onCreateCardSelected(mode: CardMode) {
        _navigationEvent.value = NavigationEvent.NavigateToCreateCard(mode)
    }

    fun onPlaySelected(mode: GameMode) {
        _navigationEvent.value = NavigationEvent.NavigateToPlay(mode)
    }

    fun onEditSelected(mode: CardMode) {
        _navigationEvent.value = NavigationEvent.NavigateToEdit(mode)
    }

    fun loadCards() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.allCards.collect { cards ->
                    _uiState.value = UiState.CardsLoaded(cards)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Erro ao carregar cards: ${e.message}")
            }
        }
    }

    fun resetNavigation() {
        _navigationEvent.value = null
    }
}