package com.rpm.core.domain

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rpm.core.domain.entity.Recipe
import java.lang.reflect.Type

class RecipeDeserializer : JsonDeserializer<Recipe> {
  override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Recipe {
    val jsonObj = json.asJsonObject

    val id = jsonObj["idMeal"]?.asString
    val meal = jsonObj["strMeal"]?.asString
    val category = jsonObj["strCategory"]?.asString
    val instructions = jsonObj["strInstructions"]?.asString
    val thumb = jsonObj["strMealThumb"]?.asString
    val youtube = jsonObj["strYoutube"]?.asString

    val ingredients = mutableListOf<String>()
    for (i in 1..20) {
      val ingredientName = jsonObj["strIngredient$i"]?.asString
      val measure = jsonObj["strMeasure$i"]?.asString

      if (!ingredientName.isNullOrBlank()) {
        ingredients.add("${ingredientName.trim()} - ${measure?.trim().orEmpty()}")
      }
    }

    return Recipe(
      id = id.orEmpty(),
      meal = meal.orEmpty(),
      category = category.orEmpty(),
      instructions = instructions.orEmpty(),
      thumb = thumb.orEmpty(),
      youtubeLink = youtube.orEmpty(),
      ingredients = ingredients,
      source = jsonObj["strSource"]?.asString.orEmpty(),
    )
  }
}
