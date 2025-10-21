package com.rpm.recipe.by.id.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpm.recipe.by.id.state.RecipeByIdUiAction
import com.rpm.recipe.by.id.state.RecipeByIdUiEffect
import com.rpm.recipe.by.id.state.RecipeByIdUiSection
import com.rpm.recipe.by.id.usecase.RecipeByIdUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeByIdViewModel(
  private val listUseCase: RecipeByIdUseCase,
) : ViewModel() {
  private val _uiState = MutableStateFlow(RecipeByIdUiSection())
  val uiState: StateFlow<RecipeByIdUiSection> = _uiState.asStateFlow()

  private val _uiEffect = MutableSharedFlow<RecipeByIdUiEffect>()
  val uiEffect: SharedFlow<RecipeByIdUiEffect> = _uiEffect.asSharedFlow()

  fun handleAction(action: RecipeByIdUiAction) {
    when (action) {
      is RecipeByIdUiAction.NavigateBack -> emit(RecipeByIdUiEffect.NavigateBack)
      is RecipeByIdUiAction.OpenYoutubeLink -> emit(RecipeByIdUiEffect.OpenYoutubeLink(action.link))
      is RecipeByIdUiAction.LoadData -> loadMealsByCategories(action.recipeId)
    }
  }

  private fun emit(effect: RecipeByIdUiEffect) = viewModelScope.launch {
    _uiEffect.emit(effect)
  }

  private fun loadMealsByCategories(recipeId: String) = viewModelScope.launch {
    _uiState.value = _uiState.value.copy(isLoading = true, error = null)
    listUseCase.invoke(recipeId).collect { result ->
      result
        .onSuccess { recipes ->
          _uiState.value = _uiState.value.copy(
            recipes = recipes.orEmpty(),
            isLoading = false,
            error = null,
          )
        }.onFailure { throwable ->
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = throwable.message ?: "Unknown error occurred",
          )
        }
    }
  }
}
