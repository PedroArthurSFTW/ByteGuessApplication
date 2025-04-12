package com.github.byteguessapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.byteguessapplication.ByteGuessApplication

@Database(entities = [Card::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    companion object {
        fun getDatabase(byteGuessApplication: ByteGuessApplication) {

        }
    }
    //TODO:companion object
}