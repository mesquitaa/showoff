package com.rpm.category.list.state

import com.rpm.core.domain.entity.Category

sealed class CategoryListUiAction {
  object LoadData : CategoryListUiAction()

  data class SelectCategory(
    val category: Category,
  ) : CategoryListUiAction()
}
