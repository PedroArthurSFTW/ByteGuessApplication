package com.github.byteguessapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.byteguessapplication.data.local.CardDao
import com.github.byteguessapplication.data.local.TipDao
import com.github.byteguessapplication.data.local.TipEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameViewModel(
    private val tipDao: TipDao,
    private val cardDao: CardDao
) : ViewModel() {

    fun getAnswerForCard(cardId: Long): Flow<String> {
        return flow {
            val answer = cardDao.getCardById(cardId)?.answer ?: "Resposta não encontrada"
            emit(answer)
        }.flowOn(viewModelScope.coroutineContext)
    }

    private val _tips = MutableStateFlow<List<TipEntity>>(emptyList())
    val tips: StateFlow<List<TipEntity>> = _tips

    private val _answers = MutableStateFlow<Map<Long, String>>(emptyMap())
    val answers: StateFlow<Map<Long, String>> = _answers

    private val _expandedCardId = MutableStateFlow<Long?>(null)
    val expandedCardId: StateFlow<Long?> = _expandedCardId

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            tipDao.getTipsForCard(1L).collect { tipsList ->
                _tips.value = tipsList
            }
        }
    }

    fun toggleCardExpansion(cardId: Long) {
        viewModelScope.launch {
            _expandedCardId.value = if (_expandedCardId.value == cardId) null else cardId

            if (_expandedCardId.value == cardId && !_answers.value.containsKey(cardId)) {
                loadAnswerForCard(cardId)
            }
        }
    }

    private suspend fun loadAnswerForCard(cardId: Long) {
        val answer = cardDao.getCardById(cardId)?.answer ?: "Resposta não encontrada"
        _answers.value = _answers.value + (cardId to answer)
    }

    val uiState = combine(_tips, _answers, _expandedCardId) { tips, answers, expandedId ->
        GameUiState(
            tips = tips,
            answers = answers,
            expandedCardId = expandedId
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameUiState()
    )
}

data class GameUiState(
    val tips: List<TipEntity> = emptyList(),
    val answers: Map<Long, String> = emptyMap(),
    val expandedCardId: Long? = null
)