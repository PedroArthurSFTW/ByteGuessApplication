package com.github.byteguessapplication.data.local

import kotlinx.coroutines.flow.Flow

class TipRepository(private val tipDao: TipDao) {
    suspend fun addTip(tip: TipEntity): Long {
        return tipDao.insert(tip)
    }

    suspend fun addTips(tips: List<TipEntity>) {
        tipDao.insertAll(tips)
    }

    suspend fun updateTip(tip: TipEntity) {
        tipDao.update(tip)
    }

    suspend fun deleteTip(tip: TipEntity) {
        tipDao.delete(tip)
    }

    fun getTipsForCard(cardId: Long): Flow<List<TipEntity>> {
        return tipDao.getTipsForCard(cardId)
    }

    suspend fun deleteTipsForCard(cardId: Long) {
        tipDao.deleteTipsForCard(cardId)
    }

}