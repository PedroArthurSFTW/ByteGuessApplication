package com.github.byteguessapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.byteguessapplication.data.local.CardEntity
import com.github.byteguessapplication.data.local.CategoryEntity
import com.github.byteguessapplication.data.local.CardRepository
import com.github.byteguessapplication.data.local.CategoryRepository
import com.github.byteguessapplication.data.local.FormErrorState
import com.github.byteguessapplication.data.local.TipEntity
import com.github.byteguessapplication.data.local.TipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCardViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val categoryRepository: CategoryRepository,
    private val tipRepository: TipRepository
) : ViewModel() {

    private val _answer = MutableStateFlow("")
    val answer: StateFlow<String> = _answer.asStateFlow()

    private val _selectedCategory = MutableStateFlow<CategoryEntity?>(null)
    val selectedCategory: StateFlow<CategoryEntity?> = _selectedCategory.asStateFlow()

    private val _availableCategories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val availableCategories: StateFlow<List<CategoryEntity>> = _availableCategories.asStateFlow()

    private val _isCategoryLightMode = MutableStateFlow(true)
    val isCategoryLightMode: StateFlow<Boolean> = _isCategoryLightMode.asStateFlow()

    private val _tips = MutableStateFlow<List<String>>(listOf(""))
    val tips: StateFlow<List<String>> = _tips.asStateFlow()

    private val _isLoadingCategories = MutableStateFlow(false)
    val isLoadingCategories: StateFlow<Boolean> = _isLoadingCategories.asStateFlow()

    private val _errorState = MutableStateFlow(FormErrorState())
    val errorState: StateFlow<FormErrorState> = _errorState.asStateFlow()

    private val _saveResult = MutableSharedFlow<Boolean>()
    val saveResult: SharedFlow<Boolean> = _saveResult.asSharedFlow()

    val isSaveEnabled: StateFlow<Boolean> =
        combine(answer, selectedCategory, tips) { ans, cat, tipList ->
            ans.isNotBlank() && cat != null && tipList.any { it.isNotBlank() } && tipList.isNotEmpty()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val MAX_TIPS = 10

    init {
        loadCategories()
    }

    fun onLightModeToggled(isLight: Boolean) {
        _isCategoryLightMode.value = isLight
    }

    fun onAnswerChanged(newAnswer: String) {
        _answer.value = newAnswer
        if (_errorState.value.answerError != null) {
            _errorState.update { it.copy(answerError = null, generalError = null) }
        }
    }

    fun onCategorySelected(category: CategoryEntity) {
        _selectedCategory.value = category
        if (_errorState.value.categoryError != null) {
            _errorState.update { it.copy(categoryError = null, generalError = null) }
        }
    }

    fun onTipChanged(index: Int, text: String) {
        val currentTips = _tips.value.toMutableList()
        if (index in currentTips.indices) {
            currentTips[index] = text
            _tips.value = currentTips
            if (_errorState.value.tipsError != null && text.isNotBlank()) {
                _errorState.update { it.copy(tipsError = null, generalError = null) }
            }
        }
    }

    fun addTipField() {
        if (_tips.value.size < MAX_TIPS) {
            _tips.value += ""
        }
    }

    fun removeTipField(index: Int) {
        val currentTips = _tips.value.toMutableList()
        if (index in currentTips.indices && currentTips.size > 1) {
            currentTips.removeAt(index)
            _tips.value = currentTips
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _isLoadingCategories.value = true
            categoryRepository.allCategories
                .catch {
                    _errorState.update { it.copy(generalError = "Could not load categories.") }
                    _isLoadingCategories.value = false
                }
                .collect { categories ->
                    _availableCategories.value = categories
                    _isLoadingCategories.value = false
                }
        }
    }

    fun saveCard() {
        viewModelScope.launch {
            if (!validateInput()) {
                _saveResult.emit(false)
                return@launch
            }

            val currentAnswer = _answer.value
            val categoryDataFromSelection = _selectedCategory.value!!
            val selectedCategoryName = categoryDataFromSelection.name
            val currentTips = _tips.value.map { it.trim() }.filter { it.isNotEmpty() }

            _errorState.update { it.copy(generalError = null) }

            try {
                var categoryIdToUse: Long
                val existingCategory = categoryRepository.getCategoryByName(selectedCategoryName)

                if (existingCategory == null) {
                    val newCategoryEntity = CategoryEntity(
                        name = selectedCategoryName,
                        isLightMode = _isCategoryLightMode.value
                    )
                    categoryIdToUse = categoryRepository.addCategory(newCategoryEntity)
                } else {
                    categoryIdToUse = existingCategory.id
                }

                val cardToInsert = CardEntity(
                    answer = currentAnswer,
                    categoryId = categoryIdToUse
                )

                val newCardId = cardRepository.addCard(card = cardToInsert)

                currentTips.forEach { tipText ->
                    val tipToInsert = TipEntity(
                        text = tipText,
                        cardId = newCardId
                    )
                    tipRepository.addTip(tip = tipToInsert)
                }

                _saveResult.emit(true)

            } catch (e: Exception) {
                _errorState.update { it.copy(generalError = "Error saving card: ${e.message}") }
                _saveResult.emit(false)
            }
        }
    }

    private suspend fun validateInput(): Boolean {
        var isValid = true
        val currentAnswer = _answer.value.trim()
        val currentCategory = _selectedCategory.value
        val nonBlankTips = _tips.value.count { it.isNotBlank() }

        val answerError = if (currentAnswer.isBlank()) {
            isValid = false
            "Answer cannot be empty."
        } else {
            if (cardRepository.doesAnswerExist(currentAnswer)) {
                isValid = false
                "This answer already exists."
            } else null
        }


        val categoryError = if (currentCategory == null) {
            isValid = false
            "Please select a category."
        } else null

        val tipsError = if (nonBlankTips == 0) {
            isValid = false
            "Please add at least one non-empty tip."
        } else null

        _errorState.value = FormErrorState(
            answerError = answerError,
            categoryError = categoryError,
            tipsError = tipsError
        )

        return isValid
    }
}