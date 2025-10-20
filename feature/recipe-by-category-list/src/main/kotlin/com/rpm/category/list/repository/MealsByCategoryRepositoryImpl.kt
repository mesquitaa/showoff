package com.rpm.category.list.repository

import com.rpm.category.list.api.MealByCategoryApi
import com.rpm.core.domain.repository.MealByCategoryRepository
import kotlinx.coroutines.flow.flow

class MealsByCategoryRepositoryImpl(
  private val api: MealByCategoryApi,
) : MealByCategoryRepository {
  override suspend fun getMealsByCategory(category: String) = flow {
    val response = api.getMealByCategory(category)
    emit(response.meals)
  }
}
