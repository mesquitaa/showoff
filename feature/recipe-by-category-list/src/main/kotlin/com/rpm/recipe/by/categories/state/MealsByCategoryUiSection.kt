package com.rpm.recipe.by.categories.state

import com.rpm.core.domain.entity.Meal

data class MealsByCategoryUiSection(
  val meals: List<Meal> = emptyList(),
  val isLoading: Boolean = false,
  val error: String? = null,
)
