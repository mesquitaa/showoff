package com.rpm.core.domain

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rpm.core.domain.entity.Recipe
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertEquals

class RecipeDeserializerTest {
  private val gson: Gson = GsonBuilder()
    .registerTypeAdapter(Recipe::class.java, RecipeDeserializer())
    .create()

  @Test
  fun `deserialize recipe`() {
    val json = """
            {
              "idMeal": "52772",
              "strMeal": "Teriyaki Chicken Casserole",
              "strCategory": "Chicken",
              "strInstructions": "Cook rice, make sauce...",
              "strMealThumb": "https://www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg",
              "strYoutube": "https://www.youtube.com/watch?v=4aZr5hZXP_s",
              "strIngredient1": "soy sauce",
              "strMeasure1": "3/4 cup",
              "strIngredient2": "water",
              "strMeasure2": "1/2 cup",
              "strSource": "https://www.themealdb.com/meal/52772"
            }
    """.trimIndent()

    val recipe = gson.fromJson(json, Recipe::class.java)

    assertEquals("52772", recipe.id)
    assertEquals("Teriyaki Chicken Casserole", recipe.meal)
    assertEquals("Chicken", recipe.category)
    assertEquals("Cook rice, make sauce...", recipe.instructions)
    assertEquals("https://www.youtube.com/watch?v=4aZr5hZXP_s", recipe.youtubeLink)
    assertEquals(2, recipe.ingredients.size)
    assertTrue(recipe.ingredients.contains("soy sauce - 3/4 cup"))
    assertTrue(recipe.ingredients.contains("water - 1/2 cup"))
  }

  @Test
  fun `should handle missing fields`() {
    val json = """{ "idMeal": "99999" }"""
    val recipe = gson.fromJson(json, Recipe::class.java)

    assertEquals("99999", recipe.id)
    assertTrue(recipe.ingredients.isEmpty())
    assertEquals("", recipe.meal)
    assertEquals("", recipe.category)
  }
}
