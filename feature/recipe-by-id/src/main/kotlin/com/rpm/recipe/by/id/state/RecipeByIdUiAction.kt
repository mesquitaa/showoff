package com.rpm.recipe.by.id.state

sealed class RecipeByIdUiAction {
  data class LoadData(
    val recipeId: String,
  ) : RecipeByIdUiAction()

  object NavigateBack : RecipeByIdUiAction()
}
