package com.rpm.category.list.state

sealed class MealsByCategoryUiEffect {
  data class NavigateToRecipeDetails(
    val recipeId: Int,
  ) : MealsByCategoryUiEffect()

  object NavigateBack : MealsByCategoryUiEffect()
}
