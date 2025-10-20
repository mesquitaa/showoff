package com.rpm.recipe.by.categories.state

import com.rpm.core.domain.entity.Meal

sealed class MealsByCategoryUiAction {
  data class LoadData(
    val meal: String,
  ) : MealsByCategoryUiAction()

  object NavigateBack : MealsByCategoryUiAction()

  data class SelectMeal(
    val meal: Meal,
  ) : MealsByCategoryUiAction()
}
