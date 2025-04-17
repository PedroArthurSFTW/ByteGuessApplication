package com.github.byteguessapplication.data.local

import kotlinx.coroutines.flow.Flow

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(private val categoryDao: CategoryDao) {

    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    suspend fun addCategory(category: CategoryEntity): Long {
        return categoryDao.insert(category)
    }

    suspend fun getCategoryByName(name: String): CategoryEntity? {
        return categoryDao.getCategoryByName(name)
    }

}