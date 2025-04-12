package com.github.byteguessapplication.data

import com.github.byteguessapplication.data.local.CategoryDao
import com.github.byteguessapplication.data.local.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {
    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()
}