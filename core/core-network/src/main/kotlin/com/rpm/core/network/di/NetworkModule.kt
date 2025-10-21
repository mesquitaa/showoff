package com.rpm.core.network.di

import com.google.gson.GsonBuilder
import com.rpm.core.domain.RecipeDeserializer
import com.rpm.core.domain.entity.Recipe
import com.rpm.core.network.BuildConfig
import com.rpm.core.network.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIME_OUT = 10L

val networkModule = module {
  single<HttpLoggingInterceptor> {
    provideLoggingInterceptor()
  }

  single<AuthInterceptor> {
    AuthInterceptor(
      token = BuildConfig.API_KEY,
    )
  }

  single {
    provideOkHttpClient(loggingInterceptor = get(), authInterceptor = get())
  }

  single {
    provideRetrofit(okHttpClient = get())
  }
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit
  .Builder()
  .baseUrl(BuildConfig.BASE_URL)
  .client(okHttpClient)
  .addConverterFactory(GsonConverterFactory.create(provideCustomGson()))
  .build()

private fun provideCustomGson() = GsonBuilder()
  .registerTypeAdapter(Recipe::class.java, RecipeDeserializer())
  .create()

private fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient
  .Builder()
  .addInterceptor(loggingInterceptor)
  .addInterceptor(authInterceptor)
  .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
  .readTimeout(TIME_OUT, TimeUnit.SECONDS)
  .build()

private fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
  level = if (BuildConfig.DEBUG) {
    HttpLoggingInterceptor.Level.BODY
  } else {
    HttpLoggingInterceptor.Level.NONE
  }
}
