package com.rpm.recipe.by.categories.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
        title = {
          Text(
            text = category,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
          )
        },
        navigationIcon = {
          IconButton(onClick = {
            viewModel.handleAction(MealsByCategoryUiAction.NavigateBack)
          }) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = stringResource(R.string.back),
            )
          }
        },
      )
    },
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
      contentAlignment = Alignment.Center,
    ) {
      when {
        uiState.isLoading -> LoadingIndicator()
        uiState.error != null -> ErrorBox(uiState.error) {
          viewModel.handleAction(MealsByCategoryUiAction.LoadData(category))
        }
        uiState.meals.isEmpty() -> EmptyState()
        else -> MealGrid(
          meals = uiState.meals,
          onMealClick = { meal ->
            viewModel.handleAction(MealsByCategoryUiAction.SelectMeal(meal))
          },
        )
      }
    }
  }
}

@Composable
private fun MealGrid(
  meals: List<Meal>,
  onMealClick: (Meal) -> Unit,
) {
  LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 160.dp),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.fillMaxSize(),
  ) {
    items(meals, key = { it.id }) { meal ->
      MealByCategoryItem(meal, onClick = { onMealClick(meal) })
    }
  }
}

@Composable
fun MealByCategoryItem(
  meal: Meal,
  onClick: () -> Unit,
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .animateContentSize()
      .clickable(onClick = onClick),
    shape = MaterialTheme.shapes.medium,
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
  ) {
    Box {
      AsyncImage(
        model = meal.thumb,
        contentDescription = stringResource(R.string.content_description_category_thumbnail),
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1f)
          .clip(MaterialTheme.shapes.medium),
        contentScale = ContentScale.Crop,
      )

      Box(
        modifier = Modifier
          .matchParentSize()
          .background(Color.Black.copy(alpha = 0.25f)),
      )

      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color.Transparent,
                Color.Black.copy(alpha = 0.75f),
              ),
              startY = 100f,
            ),
          ),
      )

      Text(
        text = meal.meal,
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(12.dp),
        style = MaterialTheme.typography.titleMedium.copy(
          color = Color.White,
          fontWeight = FontWeight.SemiBold,
          shadow = Shadow(
            color = Color.Black.copy(alpha = 0.6f),
            offset = Offset(1f, 1f),
            blurRadius = 2f,
          ),
        ),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}
