package com.rpm.category.list.ui

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.rpm.category.list.state.CategoryListUiAction
import com.rpm.category.list.state.CategoryListUiEffect
import com.rpm.category.list.viewmodel.CategoryListViewModel
import com.rpm.core.domain.entity.Category
import com.rpm.core.ui.components.EmptyState
import com.rpm.core.ui.components.ErrorBox
import com.rpm.core.ui.components.LoadingIndicator
import com.rpm.recipe.categories.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
  onNavigateToDetails: (String) -> Unit,
  viewModel: CategoryListViewModel = koinViewModel(),
) {
  val lifecycleOwner = LocalLifecycleOwner.current
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.handleAction(CategoryListUiAction.LoadData)

    lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
      viewModel.uiEffect.collect { effect ->
        when (effect) {
          is CategoryListUiEffect.NavigateToCategoryList ->
            onNavigateToDetails(effect.category)
        }
      }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Categories",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
          )
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
          viewModel.handleAction(CategoryListUiAction.LoadData)
        }

        uiState.categories.isEmpty() -> EmptyState()

        else -> CategoryGrid(
          categories = uiState.categories,
          onCategoryClick = { viewModel.handleAction(CategoryListUiAction.SelectCategory(it)) },
        )
      }
    }
  }
}

@Composable
private fun CategoryGrid(
  categories: List<Category>,
  onCategoryClick: (Category) -> Unit,
) {
  LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 160.dp),
    contentPadding = PaddingValues(16.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.fillMaxSize(),
  ) {
    items(categories, key = { it.category }) { category ->
      CategoryCard(category, onClick = { onCategoryClick(category) })
    }
  }
}

@Composable
private fun CategoryCard(
  category: Category,
  onClick: () -> Unit,
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(1f)
      .clickable(onClick = onClick)
      .animateContentSize(),
    shape = MaterialTheme.shapes.large,
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
  ) {
    Box {
      AsyncImage(
        model = category.thumb,
        contentDescription = stringResource(R.string.content_description_category_thumbnail),
        modifier = Modifier
          .fillMaxSize()
          .clip(MaterialTheme.shapes.large),
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
              listOf(
                Color.Transparent,
                Color.Black.copy(alpha = 0.75f),
              ),
              startY = 200f,
            ),
          ),
      )

      Text(
        text = category.category,
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(12.dp),
        style = MaterialTheme.typography.titleMedium.copy(
          color = Color.White,
          fontWeight = FontWeight.Bold,
          shadow = Shadow(
            color = Color.Black.copy(alpha = 0.7f),
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
