package com.github.byteguessapplication.data

import com.github.byteguessapplication.data.local.Card
import com.github.byteguessapplication.data.local.CardDao
import kotlinx.coroutines.flow.Flow

class CardRepository(private val cardDao: CardDao) {
    val allCards: Flow<List<Card>> = cardDao.getAllCards()
        //TODO:métodos CRUD
}