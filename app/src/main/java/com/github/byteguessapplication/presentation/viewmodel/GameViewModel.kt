package com.github.byteguessapplication.presentation.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.byteguessapplication.data.local.CardDao
import com.github.byteguessapplication.data.local.TipDao
import com.github.byteguessapplication.data.local.TipEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameViewModel(
    private val tipDao: TipDao,
    private val cardDao: CardDao
) : ViewModel() {

    val tips: StateFlow<List<TipEntity>> = tipDao.getTipsForCard(1L)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentAnswer = MutableStateFlow("")
    val currentAnswer: StateFlow<String> get() = _currentAnswer

    fun updateCurrentAnswer(cardId: Long) {
        viewModelScope.launch {
            val card = cardDao.getCardById(cardId)
            _currentAnswer.value = card?.answer ?: "Resposta não encontrada"
        }
    }
}