package com.rpm.category.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.rpm.category.list.state.CategoryListUiAction
import com.rpm.category.list.state.CategoryListUiEffect
import com.rpm.category.list.viewmodel.CategoryListViewModel
import com.rpm.core.domain.entity.Category
import com.rpm.core.ui.components.EmptyState
import com.rpm.core.ui.components.ErrorBox
import com.rpm.core.ui.components.LoadingIndicator
import com.rpm.recipe.categories.R
import org.koin.androidx.compose.koinViewModel

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

  when {
    uiState.isLoading -> LoadingIndicator()
    uiState.error != null -> ErrorBox(uiState.error) {
      viewModel.handleAction(CategoryListUiAction.LoadData)
    }
    uiState.categories.isEmpty() -> EmptyState()
    else -> CategoryList(uiState.categories) {
      viewModel.handleAction(CategoryListUiAction.SelectCategory(it))
    }
  }
}

@Composable
fun CategoryList(
  categories: List<Category>,
  onCategoryClick: (Category) -> Unit,
) {
  LazyColumn(
    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(categories) { category ->
      CategoryItem(
        category = category,
        onClick = {
          onCategoryClick(category)
        },
      )
    }
  }
}

@Composable
fun CategoryItem(
  category: Category,
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
    Row(
      modifier = Modifier.padding(12.dp),
    ) {
      AsyncImage(
        model = category.thumb,
        contentDescription = stringResource(id = R.string.content_description_category_thumbnail),
        modifier =
          Modifier
            .size(80.dp, 120.dp),
        contentScale = ContentScale.Crop,
      )

      Spacer(modifier = Modifier.width(12.dp))

      Column(
        modifier = Modifier.weight(1f),
      ) {
        Text(
          text = category.category,
          style = MaterialTheme.typography.titleMedium,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = category.description,
          style = MaterialTheme.typography.bodyMedium,
          maxLines = 3,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}
