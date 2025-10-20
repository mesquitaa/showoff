package com.rpm.category.list.usecase

import com.rpm.core.domain.entity.Category
import com.rpm.core.domain.repository.MealCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class CategoryListUseCase(
  private val repository: MealCategoryRepository,
) {
  suspend operator fun invoke(): Flow<Result<List<Category>?>> = flow {
    repository.getCategories().collect { response ->
      emit(Result.success(response))
    }
  }.catch { e ->
    emit(Result.failure(e))
  }
}
