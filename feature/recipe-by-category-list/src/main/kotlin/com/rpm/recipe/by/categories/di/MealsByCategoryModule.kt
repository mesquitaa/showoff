package com.rpm.recipe.by.categories.di

import com.rpm.core.domain.repository.MealByCategoryRepository
import com.rpm.recipe.by.categories.api.MealByCategoryApi
import com.rpm.recipe.by.categories.repository.MealsByCategoryRepositoryImpl
import com.rpm.recipe.by.categories.usecase.MealsByCategoryUseCase
import com.rpm.recipe.by.categories.viewmodel.MealsByCategoryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val mealsByCategoryModule = module {
  // API
  single { get<Retrofit>().create(MealByCategoryApi::class.java) }

  single {
    MealsByCategoryUseCase(
      repository = get(),
    )
  }

  // Repository
  single<MealByCategoryRepository> {
    MealsByCategoryRepositoryImpl(
      api = get(),
    )
  }

  // ViewModel
  viewModelOf(::MealsByCategoryViewModel)
}
