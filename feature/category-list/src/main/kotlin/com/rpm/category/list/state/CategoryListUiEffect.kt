package com.rpm.category.list.state

sealed class CategoryListUiEffect {
    data class NavigateToCategoryList(
        val category: String,
    ) : CategoryListUiEffect()
}
