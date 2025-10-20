package com.rpm.showoff.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

const val CATEGORY_ID = "categoryId"

sealed class Routes(
  val route: String,
  val arguments: List<NamedNavArgument> = emptyList(),
) {
  object RecipeCategoriesList : Routes(
    route = "recipeCategoriesList",
  )

  data object RecipeListByCategory : Routes(
    route = "category/{$CATEGORY_ID}",
    arguments =
      listOf(
        navArgument(CATEGORY_ID) { type = NavType.StringType },
      ),
  ) {
    fun createRoute(categoryName: String) = "category/$categoryName"
  }
}
