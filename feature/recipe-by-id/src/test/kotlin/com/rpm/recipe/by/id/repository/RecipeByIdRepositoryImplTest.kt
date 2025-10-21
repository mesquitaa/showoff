package com.rpm.recipe.by.id.repository

import com.rpm.core.domain.entity.Recipe
import com.rpm.core.domain.entity.RecipeResponse
import com.rpm.recipe.by.id.api.RecipeByIdApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RecipeByIdRepositoryImplTest {
  private val mockApi = mockk<RecipeByIdApi>(relaxed = true)

  private val subject by lazy {
    RecipeByIdRepositoryImpl(
      api = mockApi,
    )
  }

  @Test
  fun `getMealsByCategory - should return list of meals`() = runTest {
    // GIVEN
    coEvery { mockApi.getRecipeById(id = FAKE_ID) } returns RecipeResponse(
      meals = listOf(
        Recipe(
          id = FAKE_ID,
          meal = FAKE_DESCRIPTION,
          category = FAKE_CATEGORY,
          instructions = FAKE_INSTRUCTIONS,
          thumb = FAKE_THUMB,
          youtubeLink = FAKE_YOUTUBE_LINK,
          ingredients = emptyList(),
        ),
      ),
    )

    // WHEN
    val result = subject.getRecipeBy(id = FAKE_ID).single()

    // THEN
    coVerify { mockApi.getRecipeById(id = FAKE_ID) }
    assertEquals(1, result.size)
    assertEquals(FAKE_ID, result[0].id)
    assertEquals(FAKE_THUMB, result[0].thumb)
    assertEquals(FAKE_DESCRIPTION, result[0].meal)
    assertEquals(FAKE_CATEGORY, result[0].category)
    assertEquals(FAKE_YOUTUBE_LINK, result[0].youtubeLink)
    assertEquals(FAKE_INSTRUCTIONS, result[0].instructions)
    assertEquals(emptyList<String>(), result[0].ingredients)
  }

  companion object Companion {
    private const val FAKE_ID = "1"
    private const val FAKE_THUMB = "fake_thumb"
    private const val FAKE_CATEGORY = "fake_category"
    private const val FAKE_DESCRIPTION = "fake_description"
    private const val FAKE_YOUTUBE_LINK = "fake_youtube_link"
    private const val FAKE_INSTRUCTIONS = "fake_instructions"
  }
}
