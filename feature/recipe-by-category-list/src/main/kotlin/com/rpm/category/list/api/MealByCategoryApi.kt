package com.rpm.category.list.api

import com.rpm.core.domain.entity.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealByCategoryApi {
  @GET("v1/1/filter.php")
  suspend fun getMealByCategory(
    @Query("c") category: String
  ): MealResponse
}
