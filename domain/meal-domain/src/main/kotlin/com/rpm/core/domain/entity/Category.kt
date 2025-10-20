package com.rpm.core.domain.entity

import com.google.gson.annotations.SerializedName

data class Category(
  @SerializedName("idCategory") val id: Int,
  @SerializedName("strCategory") val category: String,
  @SerializedName("strCategoryThumb") val thumb: String,
  @SerializedName("strCategoryDescription") val description: String,
)
