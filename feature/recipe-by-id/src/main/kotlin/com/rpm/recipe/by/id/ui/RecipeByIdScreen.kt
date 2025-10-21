package com.rpm.recipe.by.id.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.rpm.core.domain.entity.Recipe
import com.rpm.core.ui.components.EmptyState
import com.rpm.core.ui.components.ErrorBox
import com.rpm.core.ui.components.LoadingIndicator
import com.rpm.recipe.by.id.R
import com.rpm.recipe.by.id.state.RecipeByIdUiAction
import com.rpm.recipe.by.id.state.RecipeByIdUiEffect
import com.rpm.recipe.by.id.state.RecipeByIdUiSection
import com.rpm.recipe.by.id.viewmodel.RecipeByIdViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeByIdScreen(
  recipeId: String,
  onBackPressed: () -> Unit,
  onYoutubeLinkClicked: (String) -> Unit,
  viewModel: RecipeByIdViewModel = koinViewModel(),
) {
  val title = remember { mutableStateOf("") }
  val lifecycleOwner = LocalLifecycleOwner.current
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.handleAction(RecipeByIdUiAction.LoadData(recipeId))

    lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
      viewModel.uiEffect.collect { effect ->
        when (effect) {
          is RecipeByIdUiEffect.NavigateBack -> onBackPressed()
          is RecipeByIdUiEffect.OpenYoutubeLink -> onYoutubeLinkClicked(effect.link)
        }
      }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(title.value, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = { viewModel.handleAction(RecipeByIdUiAction.NavigateBack) }) {
            Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
          }
        },
      )
    },
  ) { paddingValues ->
    when {
      uiState.isLoading -> LoadingIndicator()
      uiState.error != null -> ErrorBox(uiState.error) {
        viewModel.handleAction(RecipeByIdUiAction.LoadData(recipeId))
      }
      uiState.recipes.isEmpty() -> EmptyState()
      else -> RecipeContent(paddingValues, uiState, title, viewModel)
    }
  }
}

@Composable
private fun RecipeContent(
  paddingValues: PaddingValues,
  uiState: RecipeByIdUiSection,
  title: MutableState<String>,
  viewModel: RecipeByIdViewModel,
) {
  val recipe = uiState.recipes.first()
  title.value = recipe.meal

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(bottom = 24.dp),
    contentPadding = paddingValues,
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item { RecipeHeader(recipe) }
    item { RecipeInfoSection(recipe) }
    item { RecipeListIngredients(recipe) }
    item { RecipeListInstructions(recipe) }
    item { RecipeYoutubeButton { viewModel.handleAction(RecipeByIdUiAction.OpenYoutubeLink(recipe.youtubeLink)) } }
  }
}

@Composable
private fun RecipeHeader(recipe: Recipe) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(280.dp),
  ) {
    AsyncImage(
      model = recipe.thumb,
      contentDescription = recipe.meal,
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop,
    )

    Box(
      modifier = Modifier
        .matchParentSize()
        .background(
          Brush.verticalGradient(
            listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
            startY = 150f,
          ),
        ),
    )

    Column(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(16.dp),
    ) {
      Text(
        recipe.meal,
        style = MaterialTheme.typography.headlineSmall.copy(
          color = Color.White,
          fontWeight = FontWeight.Bold,
          shadow = Shadow(
            color = Color.Black.copy(alpha = 0.6f),
            offset = Offset(2f, 2f),
            blurRadius = 4f,
          ),
        ),
      )
      Text(
        recipe.category,
        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.85f)),
      )
    }
  }
}

@Composable
private fun RecipeInfoSection(recipe: Recipe) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    shape = RoundedCornerShape(20.dp),
    elevation = CardDefaults.cardElevation(6.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = stringResource(R.string.overview),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
      )
      Spacer(Modifier.height(8.dp))
      Text(
        text = recipe.instructions,
        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
      )
    }
  }
}

@Composable
private fun RecipeListIngredients(recipe: Recipe) {
  SectionCard(title = stringResource(R.string.ingredients)) {
    recipe.ingredients.forEach { ingredient ->
      Text(
        "• $ingredient",
        style = MaterialTheme.typography.bodyLarge.copy(
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        modifier = Modifier.padding(vertical = 2.dp),
      )
    }
  }
}

@Composable
private fun RecipeListInstructions(recipe: Recipe) {
  SectionCard(title = stringResource(R.string.how_to_prepare)) {
    Text(
      text = recipe.instructions,
      style = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 22.sp,
      ),
    )
  }
}

@Composable
private fun SectionCard(
  title: String,
  content: @Composable ColumnScope.() -> Unit,
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    shape = RoundedCornerShape(20.dp),
    elevation = CardDefaults.cardElevation(4.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  ) {
    Column(Modifier.padding(16.dp)) {
      Text(
        title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
      )
      Spacer(Modifier.height(8.dp))
      content()
    }
  }
}

@Composable
private fun RecipeYoutubeButton(onClick: () -> Unit) {
  Button(
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
    shape = RoundedCornerShape(50),
    modifier = Modifier
      .padding(horizontal = 32.dp, vertical = 16.dp)
      .fillMaxWidth()
      .height(50.dp),
  ) {
    Icon(
      imageVector = Icons.Default.PlayArrow,
      contentDescription = null,
      tint = Color.White,
    )
    Spacer(Modifier.width(8.dp))
    Text(
      text = stringResource(R.string.see_on_youtube),
      color = Color.White,
      style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
    )
  }
}
