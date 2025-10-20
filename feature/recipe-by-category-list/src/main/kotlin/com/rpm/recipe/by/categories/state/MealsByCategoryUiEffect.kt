package com.rpm.recipe.by.categories.state

sealed class MealsByCategoryUiEffect {
  data class NavigateToRecipeDetails(
    val recipeId: Int,
  ) : MealsByCategoryUiEffect()

  object NavigateBack : MealsByCategoryUiEffect()
}
