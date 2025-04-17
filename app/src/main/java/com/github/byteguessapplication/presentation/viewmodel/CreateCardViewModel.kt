package com.github.byteguessapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.byteguessapplication.data.local.CardEntity
import com.github.byteguessapplication.data.local.CategoryEntity
import com.github.byteguessapplication.data.local.CardRepository
import com.github.byteguessapplication.data.local.CategoryRepository
import com.github.byteguessapplication.data.local.TipEntity
import com.github.byteguessapplication.data.local.TipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class FormErrorState(
    val answerError: String? = null,
    val categoryError: String? = null,
    val tipsError: String? = null,
    val generalError: String? = null
)

@HiltViewModel
class CreateCardViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val categoryRepository: CategoryRepository,
    private val tipRepository: TipRepository
) : ViewModel() {

    // State Flows
    private val _answer = MutableStateFlow("")
    val answer: StateFlow<String> = _answer.asStateFlow()
    var isCategoryLightMode: Boolean = false // Ou baseada em alguma lógica

    private val _selectedCategory = MutableStateFlow<CategoryEntity?>(null)
    val selectedCategory: StateFlow<CategoryEntity?> = _selectedCategory.asStateFlow()

    private val _availableCategories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val availableCategories: StateFlow<List<CategoryEntity>> = _availableCategories.asStateFlow()

    private val _tips = MutableStateFlow<List<String>>(listOf(""))
    val tips: StateFlow<List<String>> = _tips.asStateFlow()

    private val _isLoadingCategories = MutableStateFlow(false)
    val isLoadingCategories: StateFlow<Boolean> = _isLoadingCategories.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _errorState = MutableStateFlow(FormErrorState())
    val errorState: StateFlow<FormErrorState> = _errorState.asStateFlow()

    private val _saveResult = MutableSharedFlow<Boolean>()
    val saveResult: SharedFlow<Boolean> = _saveResult.asSharedFlow()

    val isSaveEnabled: StateFlow<Boolean> = combine(
        answer,
        selectedCategory,
        tips,
        isSaving
    ) { ans, cat, tipList, saving ->
        !saving && ans.isNotBlank() && cat != null && tipList.any { it.isNotBlank() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = false
    )

    private val MAX_TIPS = 10
    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent
    fun navigateToCreateCard() {
        _navigationEvent.value = NavigationEvent.NavigateToCreateCard
    }


    sealed class NavigationEvent {
        object NavigateToCreateCard : NavigationEvent()
    }

    fun resetNavigation() {
        _navigationEvent.value = null
    }


    init {
        loadCategories()
    }

    fun onAnswerChanged(newAnswer: String) {
        _answer.value = newAnswer
        clearErrorIf { it.answerError != null }
    }

    fun onCategorySelected(category: CategoryEntity) {
        _selectedCategory.value = category
        clearErrorIf { it.categoryError != null }
    }

    fun onTipChanged(index: Int, text: String) {
        val currentTips = _tips.value.toMutableList()
        if (index in currentTips.indices) {
            currentTips[index] = text
            _tips.value = currentTips
            if (_errorState.value.tipsError != null && text.isNotBlank()) {
                clearErrorIf { it.tipsError != null }
            }
        }
    }

    fun addTipField() {
        if (_tips.value.size < MAX_TIPS) {
            _tips.value = _tips.value + ""
        }
    }

    fun removeTipField(index: Int) {
        val currentTips = _tips.value.toMutableList()
        if (index in currentTips.indices && currentTips.size > 1) {
            currentTips.removeAt(index)
            _tips.value = currentTips
        }
    }

    fun saveCard() {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                if (!validateInput()) {
                    _saveResult.emit(false)
                    return@launch
                }

                val currentAnswer = _answer.value.trim()
                val currentCategory = _selectedCategory.value!!
                val currentTips = _tips.value.map { it.trim() }.filter { it.isNotEmpty() }

                // Save card
                val cardId = cardRepository.addCard(
                    CardEntity(
                        answer = currentAnswer,
                        categoryId = 1
                    )
                )

                // Save tips
                currentTips.forEach { tipText ->
                    tipRepository.addTip(
                        TipEntity(
                            text = tipText,
                            cardId = cardId
                        )
                    )
                }

                _saveResult.emit(true)
            } catch (e: Exception) {
                _errorState.update {
                    it.copy(generalError = "Error saving card: ${e.localizedMessage ?: "Unknown error"}")
                }
                _saveResult.emit(false)
            } finally {
                _isSaving.value = false
            }
        }
    }

    private suspend fun validateInput(): Boolean {
        return withContext(Dispatchers.Default) {
            var isValid = true
            val currentAnswer = _answer.value.trim()
            val currentCategory = _selectedCategory.value
            val nonBlankTips = _tips.value.count { it.isNotBlank() }

            val newErrorState = FormErrorState(
                answerError = when {
                    currentAnswer.isBlank() -> "Answer cannot be empty.".also { isValid = false }
                    cardRepository.doesAnswerExist(currentAnswer) ->
                        "This answer already exists.".also { isValid = false }
                    else -> null
                },
                categoryError = currentCategory?.let { null }
                    ?: "Please select a category.".also { isValid = false },
                tipsError = if (nonBlankTips == 0) {
                    isValid = false
                    "Please add at least one non-empty tip."
                } else null
            )

            _errorState.update { newErrorState }
            isValid
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _isLoadingCategories.value = true
            try {
                categoryRepository.allCategories
                    .catch { e ->
                        _errorState.update {
                            it.copy(generalError = "Could not load categories: ${e.message}")
                        }
                    }
                    .collect { categories ->
                        _availableCategories.value = categories
                    }
            } finally {
                _isLoadingCategories.value = false
            }
        }
    }

    private fun clearErrorIf(predicate: (FormErrorState) -> Boolean) {
        if (predicate(_errorState.value)) {
            _errorState.update {
                it.copy(
                    answerError = null,
                    categoryError = null,
                    tipsError = null,
                    generalError = null
                )
            }
        }
    }
}