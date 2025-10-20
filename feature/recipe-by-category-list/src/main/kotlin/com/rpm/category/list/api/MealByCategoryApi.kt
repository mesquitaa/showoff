package com.rpm.category.list.api

import com.rpm.core.domain.entity.MealResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MealByCategoryApi {
  @GET("v1/1/filter.php?c={category}")
  suspend fun getMealByCategory(
    @Path("category") category: String,
  ): MealResponse
}
