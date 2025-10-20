package com.rpm.showoff.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

const val CATEGORY_NAME = "categoryName"
const val RECIPE_ID = "categoryId"

sealed class Routes(
  val route: String,
  val arguments: List<NamedNavArgument> = emptyList(),
) {
  object RecipeCategoriesList : Routes(
    route = "recipeCategoriesList",
  )

  data object RecipeListByCategory : Routes(
    route = "category/{$CATEGORY_NAME}",
    arguments =
      listOf(
        navArgument(CATEGORY_NAME) { type = NavType.StringType },
      ),
  ) {
    fun createRoute(categoryName: String) = "category/$categoryName"
  }

  data object MealRecipe : Routes(
    route = "meal/{$RECIPE_ID}",
    arguments =
      listOf(
        navArgument(RECIPE_ID) { type = NavType.StringType },
      ),
  ) {
    fun createRoute(mealId: Int) = "meal/$mealId"
  }
}
