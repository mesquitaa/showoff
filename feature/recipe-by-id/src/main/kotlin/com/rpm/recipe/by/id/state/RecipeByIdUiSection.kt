package com.rpm.recipe.by.id.state

import com.rpm.core.domain.entity.Recipe

data class RecipeByIdUiSection(
  val recipes: List<Recipe> = emptyList(),
  val isLoading: Boolean = false,
  val error: String? = null,
)
