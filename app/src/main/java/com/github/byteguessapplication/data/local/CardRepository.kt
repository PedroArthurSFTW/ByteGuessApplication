package com.github.byteguessapplication.data.local

import kotlinx.coroutines.flow.Flow

class CardRepository(private val cardDao: CardDao) {
    val allCards: Flow<List<CardEntity>> = cardDao.getAllCards()

    suspend fun doesAnswerExist(answer: String): Boolean {
        return cardDao.doesAnswerExist(answer)
    }

    suspend fun addCard(card: CardEntity): Long {
        return cardDao.insert(card)
    }
}