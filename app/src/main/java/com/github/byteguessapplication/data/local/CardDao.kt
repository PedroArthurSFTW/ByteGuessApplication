package com.github.byteguessapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(card: CardEntity): Long

    @Update
    suspend fun update(card: CardEntity)

    @Delete
    suspend fun delete(card: CardEntity)

    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun getCardById(id: Long): CardEntity?

    @Query("SELECT * FROM cards WHERE category_id = :categoryId ORDER BY answer ASC")
    fun getCardsByCategory(categoryId: Long): Flow<List<CardEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM cards WHERE answer = :answer LIMIT 1)")
    suspend fun doesAnswerExist(answer: String): Boolean

    @Query("SELECT * FROM cards ORDER BY answer ASC")
    fun getAllCards(): Flow<List<CardEntity>>
}