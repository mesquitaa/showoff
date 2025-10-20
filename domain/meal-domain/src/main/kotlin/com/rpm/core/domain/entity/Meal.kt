package com.rpm.core.domain.entity

import com.google.gson.annotations.SerializedName

data class Meal(
  @SerializedName("idMeal") val id: Int,
  @SerializedName("strMeal") val meal: String,
  @SerializedName("strMealThumb") val thumb: String,
)
