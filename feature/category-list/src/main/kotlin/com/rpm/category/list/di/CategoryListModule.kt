package com.rpm.category.list.di

import com.rpm.category.list.api.CategoryListApi
import com.rpm.category.list.repository.CategoryListRepository
import com.rpm.category.list.usecase.CategoryListUseCase
import com.rpm.category.list.viewmodel.CategoryListViewModel
import com.rpm.core.domain.repository.MealCategoryRepository
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val categoryListModule = module {
  // API
  single { get<Retrofit>().create(CategoryListApi::class.java) }

  single {
    CategoryListUseCase(
      repository = get(),
    )
  }

  // Repository
  single<MealCategoryRepository> {
    CategoryListRepository(
      api = get(),
    )
  }

  // ViewModel
  viewModelOf(::CategoryListViewModel)
}
