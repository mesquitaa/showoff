package com.rpm.category.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpm.category.list.state.CategoryListUiAction
import com.rpm.category.list.state.CategoryListUiEffect
import com.rpm.category.list.state.CategoryListUiSection
import com.rpm.category.list.usecase.CategoryListUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryListViewModel(
  private val listUseCase: CategoryListUseCase,
) : ViewModel() {
  private val _uiState = MutableStateFlow(CategoryListUiSection())
  val uiState: StateFlow<CategoryListUiSection> = _uiState.asStateFlow()

  private val _uiEffect = MutableSharedFlow<CategoryListUiEffect>()
  val uiEffect: SharedFlow<CategoryListUiEffect> = _uiEffect.asSharedFlow()

  fun handleAction(action: CategoryListUiAction) {
    when (action) {
      is CategoryListUiAction.LoadData -> loadCategories()
      is CategoryListUiAction.SelectCategory -> viewModelScope.launch {
        _uiEffect.emit(CategoryListUiEffect.NavigateToCategoryList(action.category.category))
      }
    }
  }

  private fun loadCategories() = viewModelScope.launch {
    _uiState.value = _uiState.value.copy(isLoading = true, error = null)
    listUseCase.invoke().collect { result ->
      result
        .onSuccess { categories ->
          _uiState.value = _uiState.value.copy(
            categories = categories.orEmpty(),
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
