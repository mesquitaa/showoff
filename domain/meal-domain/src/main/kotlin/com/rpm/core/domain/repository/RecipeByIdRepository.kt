package com.rpm.core.domain.repository

import com.rpm.core.domain.entity.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeByIdRepository {
  suspend fun getRecipeBy(id: String) : Flow<List<Recipe>>
}
