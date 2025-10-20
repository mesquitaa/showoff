package com.rpm.recipe.by.categories.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.rpm.core.domain.entity.Meal
import com.rpm.core.ui.components.EmptyState
import com.rpm.core.ui.components.ErrorBox
import com.rpm.core.ui.components.LoadingIndicator
import com.rpm.recipe.by.categories.R
import com.rpm.recipe.by.categories.state.MealsByCategoryUiAction
import com.rpm.recipe.by.categories.state.MealsByCategoryUiEffect
import com.rpm.recipe.by.categories.viewmodel.MealsByCategoryViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealByCategoryScreen(
  category: String,
  onBackPressed: () -> Unit,
  onNavigateToRecipe: (Int) -> Unit,
  viewModel: MealsByCategoryViewModel = koinViewModel(),
) {
  val lifecycleOwner = LocalLifecycleOwner.current
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.handleAction(MealsByCategoryUiAction.LoadData(category))

    lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
      viewModel.uiEffect.collect { effect ->
        when (effect) {
          is MealsByCategoryUiEffect.NavigateBack -> onBackPressed()
          is MealsByCategoryUiEffect.NavigateToRecipeDetails -> onNavigateToRecipe(effect.recipeId)
        }
      }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(category) },
        navigationIcon = {
          IconButton(onClick = { viewModel.handleAction(MealsByCategoryUiAction.NavigateBack) }) {
            Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
          }
        },
      )
    },
  ) { paddingValues ->
    when {
      uiState.isLoading -> LoadingIndicator()
      uiState.error != null -> ErrorBox(uiState.error) {
        viewModel.handleAction(MealsByCategoryUiAction.LoadData(category))
      }
      uiState.meals.isEmpty() -> EmptyState()
      else -> {
        LazyVerticalGrid(
          columns = GridCells.Fixed(2),
          modifier = Modifier.fillMaxSize(),
          contentPadding = paddingValues,
          verticalArrangement = Arrangement.spacedBy(16.dp),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          items(uiState.meals, key = { it.id }) { meal ->
            MealByCategoryItem(meal) {
              viewModel.handleAction(MealsByCategoryUiAction.SelectMeal(meal))
            }
          }
        }
      }
    }
  }
}

@Composable
fun MealByCategoryItem(
  meal: Meal,
  onClick: () -> Unit,
) {
  Card(
    modifier =
      Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.background,
    ),
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
    ) {
      AsyncImage(
        model = meal.thumb,
        contentDescription = stringResource(id = R.string.content_description_category_thumbnail),
        modifier =
          Modifier
            .fillMaxWidth(1f),
        contentScale = ContentScale.Crop,
      )

      Text(
        modifier = Modifier.padding(top = 16.dp),
        text = meal.meal,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}
