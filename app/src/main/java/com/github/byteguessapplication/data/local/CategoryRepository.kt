package com.github.byteguessapplication.data.local

import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {

    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()
}