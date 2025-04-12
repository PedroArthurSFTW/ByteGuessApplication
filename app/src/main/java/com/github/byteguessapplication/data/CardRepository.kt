package com.github.byteguessapplication.data

import com.github.byteguessapplication.data.local.CardDao
import com.github.byteguessapplication.data.local.CardEntity
import kotlinx.coroutines.flow.Flow

class CardRepository(private val cardDao: CardDao) {
    val allCards: Flow<List<CardEntity>> = cardDao.getAllCards()

}