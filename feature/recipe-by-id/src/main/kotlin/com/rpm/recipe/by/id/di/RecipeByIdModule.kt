package com.rpm.recipe.by.id.di

import com.rpm.core.domain.repository.RecipeByIdRepository
import com.rpm.recipe.by.id.api.RecipeByIdApi
import com.rpm.recipe.by.id.repository.RecipeByIdRepositoryImpl
import com.rpm.recipe.by.id.usecase.RecipeByIdUseCase
import com.rpm.recipe.by.id.viewmodel.RecipeByIdViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val recipeByIdModule = module {
  // API
  single { get<Retrofit>().create(RecipeByIdApi::class.java) }

  single {
    RecipeByIdUseCase(
      repository = get(),
    )
  }

  // Repository
  single<RecipeByIdRepository> {
    RecipeByIdRepositoryImpl(
      api = get(),
    )
  }

  // ViewModel
  viewModelOf(::RecipeByIdViewModel)
}
