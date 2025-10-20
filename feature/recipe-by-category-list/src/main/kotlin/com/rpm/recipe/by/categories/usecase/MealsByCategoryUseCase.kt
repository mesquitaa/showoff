package com.rpm.recipe.by.categories.usecase

import com.rpm.core.domain.entity.Meal
import com.rpm.core.domain.repository.MealByCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MealsByCategoryUseCase(
  private val repository: MealByCategoryRepository,
) {
  suspend operator fun invoke(category: String): Flow<Result<List<Meal>?>> = flow {
    repository.getMealsByCategory(category).collect { response ->
      emit(Result.success(response))
    }
  }.catch { e ->
    emit(Result.failure(e))
  }
}
