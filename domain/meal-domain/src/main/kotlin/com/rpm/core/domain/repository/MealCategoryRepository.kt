package com.rpm.core.domain.repository

import com.rpm.core.domain.entity.Category
import kotlinx.coroutines.flow.Flow

interface MealCategoryRepository {
  suspend fun getCategories(): Flow<List<Category>>
}
