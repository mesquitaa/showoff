package com.rpm.showoff.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rpm.category.list.ui.CategoryListScreen

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
      Text("Recipe Categories By Category")
    }
  }
}
