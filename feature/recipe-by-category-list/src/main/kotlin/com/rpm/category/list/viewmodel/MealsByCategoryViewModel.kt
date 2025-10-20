package com.rpm.category.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpm.category.list.state.MealsByCategoryUiAction
import com.rpm.category.list.state.MealsByCategoryUiEffect
import com.rpm.category.list.state.MealsByCategoryUiSection
import com.rpm.category.list.usecase.MealsByCategoryUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MealsByCategoryViewModel(
  private val listUseCase: MealsByCategoryUseCase,
) : ViewModel() {
  private val _uiState = MutableStateFlow(MealsByCategoryUiSection())
  val uiState: StateFlow<MealsByCategoryUiSection> = _uiState.asStateFlow()

  private val _uiEffect = MutableSharedFlow<MealsByCategoryUiEffect>()
  val uiEffect: SharedFlow<MealsByCategoryUiEffect> = _uiEffect.asSharedFlow()

  fun handleAction(action: MealsByCategoryUiAction) {
    when (action) {
      is MealsByCategoryUiAction.NavigateBack -> emit(MealsByCategoryUiEffect.NavigateBack)
      is MealsByCategoryUiAction.LoadData -> loadMealsByCategories(action.meal)
      is MealsByCategoryUiAction.SelectMeal -> emit(MealsByCategoryUiEffect.NavigateToRecipeDetails(action.meal.id))
    }
  }

  private fun emit(effect: MealsByCategoryUiEffect) = viewModelScope.launch {
    _uiEffect.emit(effect)
  }

  private fun loadMealsByCategories(meal: String) = viewModelScope.launch {
    _uiState.value = _uiState.value.copy(isLoading = true, error = null)
    listUseCase.invoke(meal).collect { result ->
      result
        .onSuccess { meals ->
          _uiState.value = _uiState.value.copy(
            meals = meals.orEmpty(),
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
