package com.rpm.recipe.by.id.usecase

import com.rpm.core.domain.entity.Recipe
import com.rpm.core.domain.repository.RecipeByIdRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class RecipeByIdUseCase(
  private val repository: RecipeByIdRepository,
) {
  suspend operator fun invoke(category: String): Flow<Result<List<Recipe>?>> = flow {
    repository.getRecipeBy(category).collect { response ->
      emit(Result.success(response))
    }
  }.catch { e ->
    emit(Result.failure(e))
  }
}
