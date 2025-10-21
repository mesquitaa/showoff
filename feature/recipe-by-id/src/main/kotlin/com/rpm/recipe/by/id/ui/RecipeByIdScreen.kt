package com.rpm.recipe.by.id.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.rememberAsyncImagePainter
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
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = paddingValues,
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    items(uiState.recipes, key = { it.id }) { meal ->
      title.value = meal.meal
      RecipeListItem(meal, viewModel)
    }
  }
}

@Composable
private fun RecipeListItem(recipe: Recipe, viewModel: RecipeByIdViewModel) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.background),
        ),
      ),
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      RecipeListItemHeader(recipe)

      Spacer(Modifier.height(20.dp))

      Text(
        recipe.meal,
        style = MaterialTheme.typography.headlineSmall.copy(
          fontWeight = FontWeight.Bold,
          color = Color(0xFF5D4037),
        ),
        textAlign = TextAlign.Center,
      )

      Text(
        text = recipe.category,
        color = Color(0xFFD84315),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 4.dp),
      )

      Spacer(Modifier.height(24.dp))

      RecipeListIngredients(recipe)

      Spacer(Modifier.height(24.dp))

      RecipeListInstructions(recipe)

      Spacer(Modifier.height(24.dp))

      recipe.youtubeLink.let { url ->
        Button(
          onClick = {
            viewModel.handleAction(RecipeByIdUiAction.OpenYoutubeLink(url))
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF7043),
          ),
          shape = RoundedCornerShape(50),
          modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(50.dp),
        ) {
          Text(stringResource(R.string.see_on_youtube), color = Color.White)
        }
      }

      Spacer(Modifier.height(32.dp))
    }
  }
}

@Composable
private fun RecipeListInstructions(recipe: Recipe) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    elevation = CardDefaults.cardElevation(4.dp),
    shape = RoundedCornerShape(16.dp),
  ) {
    Column(Modifier.padding(16.dp)) {
      Text(
        stringResource(R.string.how_to_prepare),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
      )
      Spacer(Modifier.height(8.dp))
      Text(
        text = recipe.instructions,
        style = MaterialTheme.typography.bodyLarge,
        color = Color(0xFF4E342E),
      )
    }
  }
}

@Composable
private fun RecipeListIngredients(recipe: Recipe) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    elevation = CardDefaults.cardElevation(4.dp),
    shape = RoundedCornerShape(16.dp),
  ) {
    Column(Modifier.padding(16.dp)) {
      Text(
        stringResource(R.string.ingredients),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
      )

      Spacer(Modifier.height(8.dp))

      recipe.ingredients.forEach { ingredient ->
        Text(stringResource(R.string.bullet_point, ingredient), color = Color(0xFF3E2723))
      }
    }
  }
}

@Composable
private fun RecipeListItemHeader(meal: Recipe) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .height(260.dp)
      .clip(RoundedCornerShape(24.dp))
      .shadow(8.dp, RoundedCornerShape(24.dp)),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
  ) {
    Image(
      painter = rememberAsyncImagePainter(meal.thumb),
      contentDescription = meal.meal,
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop,
    )
  }
}
