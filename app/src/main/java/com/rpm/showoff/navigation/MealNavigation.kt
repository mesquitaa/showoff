package com.rpm.showoff.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rpm.category.list.ui.CategoryListScreen
import com.rpm.category.list.ui.MealByCategoryScreen

@Composable
fun MealNavigation(navController: NavHostController = rememberNavController()) {
  NavHost(
    navController = navController,
    startDestination = Routes.RecipeCategoriesList.route,
  ) {
    composable(Routes.RecipeCategoriesList.route) {
      CategoryListScreen(
        onNavigateToDetails = { categoryId ->
          navController.navigate(Routes.RecipeListByCategory.createRoute(categoryId))
        },
      )
    }

    composable(
      route = Routes.RecipeListByCategory.route,
      arguments = Routes.RecipeListByCategory.arguments,
    ) { backStackEntry ->
      val categoryName = backStackEntry.arguments?.getString(CATEGORY_NAME).orEmpty()

      MealByCategoryScreen(
        category = categoryName,
        onNavigateToRecipe = { mealId ->
          navController.navigate(Routes.MealRecipe.createRoute(mealId))
        },
        onBackPressed = navController::popBackStack,
      )
    }

    composable(Routes.MealRecipe.route) { backStackEntry ->
      val recipeId = backStackEntry.arguments?.getString(RECIPE_ID)?.toInt() ?: 0
      Text(text = "Meal Recipe $recipeId")
    }
  }
}
