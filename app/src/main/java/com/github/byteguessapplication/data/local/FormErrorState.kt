package com.github.byteguessapplication.data.local

data class FormErrorState(
    val answerError: String? = null,
    val categoryError: String? = null,
    val tipsError: String? = null,
    val generalError: String? = null
)