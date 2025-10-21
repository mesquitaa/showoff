package com.rpm.recipe.by.id.repository

import com.rpm.core.domain.repository.RecipeByIdRepository
import com.rpm.recipe.by.id.api.RecipeByIdApi
import kotlinx.coroutines.flow.flow

class RecipeByIdRepositoryImpl(
  private val api: RecipeByIdApi,
) : RecipeByIdRepository {
  override suspend fun getRecipeBy(id: String) = flow {
    val response = api.getRecipeById(id = id)
    emit(response.meals)
  }
}
