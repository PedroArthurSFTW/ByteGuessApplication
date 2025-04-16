package com.github.byteguessapplication

import android.app.Application
import com.github.byteguessapplication.data.local.CardRepository
import com.github.byteguessapplication.data.local.AppDatabase
import com.github.byteguessapplication.data.local.CategoryRepository
import com.github.byteguessapplication.data.local.TipRepository

class ByteGuessApplication : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val cardRepository by lazy { CardRepository(database.cardDao(),) }
    val categoryRepository by lazy { CategoryRepository(database.categoryDao(),) }
    val tipRepository by lazy { TipRepository(database.tipDao(),) }
}