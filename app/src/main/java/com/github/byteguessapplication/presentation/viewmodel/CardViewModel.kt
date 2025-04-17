package com.github.byteguessapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class CardViewModel @Inject constructor() : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        data class Success(val message: String? = null) : UiState()
        data class Error(val error: String) : UiState()
    }

    sealed class NavigationEvent {
        object NavigateToCreateCardScreen : NavigationEvent()
        object NavigateToPlayScreen : NavigationEvent()
        object NavigateToEditScreen : NavigationEvent()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Success())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()

    fun requestCreateCard() {
        _navigationEvent.value = NavigationEvent.NavigateToCreateCardScreen
    }

    fun requestPlay() {
        _navigationEvent.value = NavigationEvent.NavigateToPlayScreen
    }

    fun requestEditCard() {
        _navigationEvent.value = NavigationEvent.NavigateToEditScreen
    }

    fun resetNavigation() {
        _navigationEvent.value = null
    }
}