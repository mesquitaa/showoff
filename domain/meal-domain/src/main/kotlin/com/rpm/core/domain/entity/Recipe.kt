package com.rpm.core.domain.entity

data class Recipe(
  val id: String,
  val meal: String,
  val thumb: String,
  val category: String,
  val instructions: String,
  val youtubeLink: String,
  val ingredients: List<String>,
)
