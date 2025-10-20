package com.rpm.category.list.di

import com.rpm.category.list.api.MealByCategoryApi
import com.rpm.category.list.repository.MealsByCategoryRepositoryImpl
import com.rpm.category.list.usecase.MealsByCategoryUseCase
import com.rpm.category.list.viewmodel.MealsByCategoryViewModel
import com.rpm.core.domain.repository.MealByCategoryRepository
import com.rpm.core.domain.repository.MealCategoryRepository
import org.koin.core.module.dsl.singleOf
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
