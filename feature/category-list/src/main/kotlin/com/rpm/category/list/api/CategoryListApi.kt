package com.rpm.category.list.api

import com.rpm.core.domain.entity.CategoryListResponse
import retrofit2.http.GET

interface CategoryListApi {
  @GET("v1/1/categories.php")
  suspend fun getCategories(): CategoryListResponse
}
