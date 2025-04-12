package com.github.byteguessapplication.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TipDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(tip: TipEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(tips: List<TipEntity>)

    @Update
    suspend fun update(tip: TipEntity)

    @Delete
    suspend fun delete(tip: TipEntity)

    @Query("SELECT * FROM tips WHERE card_id = :cardId ORDER BY id ASC")
    fun getTipsForCard(cardId: Long): Flow<List<TipEntity>>

    @Query("DELETE FROM tips WHERE card_id = :cardId")
    suspend fun deleteTipsForCard(cardId: Long)
}