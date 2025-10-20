package com.rpm.core.domain.repository

import com.rpm.core.domain.entity.Meal
import kotlinx.coroutines.flow.Flow

interface MealByCategoryRepository {
  suspend fun getMealsByCategory(category: String): Flow<List<Meal>>
}
