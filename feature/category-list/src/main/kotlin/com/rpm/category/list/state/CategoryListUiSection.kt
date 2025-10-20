package com.rpm.category.list.state

import com.rpm.core.domain.entity.Category

data class CategoryListUiSection(
  val categories: List<Category> = emptyList(),
  val isLoading: Boolean = false,
  val error: String? = null,
)
