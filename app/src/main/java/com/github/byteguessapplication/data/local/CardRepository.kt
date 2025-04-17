package com.github.byteguessapplication.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(private val cardDao: CardDao) {
    val allCards: Flow<List<CardEntity>> = cardDao.getAllCards()

    suspend fun doesAnswerExist(answer: String): Boolean {
        return cardDao.doesAnswerExist(answer)
    }

    suspend fun addCard(card: CardEntity): Long {
        return cardDao.insert(card)
    }
}