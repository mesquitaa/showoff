package com.rpm.recipe.by.categories.repository

import com.rpm.core.domain.repository.MealByCategoryRepository
import com.rpm.recipe.by.categories.api.MealByCategoryApi
import kotlinx.coroutines.flow.flow

class MealsByCategoryRepositoryImpl(
  private val api: MealByCategoryApi,
) : MealByCategoryRepository {
  override suspend fun getMealsByCategory(category: String) = flow {
    val response = api.getMealByCategory(category)
    emit(response.meals)
  }
}
