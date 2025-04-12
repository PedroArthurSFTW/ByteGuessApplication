package com.github.byteguessapplication

import android.app.Application
import com.github.byteguessapplication.data.CardRepository
import com.github.byteguessapplication.data.local.AppDatabase

class ByteGuessApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { CardRepository(database.cardDao()) }
}