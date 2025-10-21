package com.rpm.recipe.by.id.api

import com.rpm.core.domain.entity.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeByIdApi {
  @GET("v1/1/lookup.php")
  suspend fun getRecipeById(
    @Query("i") id: String,
  ): RecipeResponse
}
