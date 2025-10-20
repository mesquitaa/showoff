package com.rpm.showoff.application

import android.app.Application
import com.rpm.category.list.di.categoryListModule
import com.rpm.core.network.di.networkModule
import com.rpm.recipe.by.categories.di.mealsByCategoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MealApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidLogger()
      androidContext(this@MealApplication)
      modules(networkModule, categoryListModule, mealsByCategoryModule)
    }
  }
}
