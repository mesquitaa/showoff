package com.rpm.category.list.repository

import com.rpm.category.list.api.CategoryListApi
import com.rpm.core.domain.entity.Category
import com.rpm.core.domain.repository.MealCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryListRepository(
  private val api: CategoryListApi,
) : MealCategoryRepository {
  override suspend fun getCategories(): Flow<List<Category>> = flow {
    val response = api.getCategories()
    emit(response.categories)
  }
}
