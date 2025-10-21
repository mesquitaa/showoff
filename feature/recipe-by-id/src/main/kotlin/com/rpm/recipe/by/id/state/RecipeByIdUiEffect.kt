package com.rpm.recipe.by.id.state

sealed class RecipeByIdUiEffect {
  data class OpenYoutubeLink(
    val link: String,
  ) : RecipeByIdUiEffect()

  object NavigateBack : RecipeByIdUiEffect()
}
