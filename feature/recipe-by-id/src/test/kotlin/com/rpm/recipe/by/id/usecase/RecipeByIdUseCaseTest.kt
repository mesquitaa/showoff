package com.rpm.recipe.by.id.usecase

import com.rpm.core.domain.entity.Recipe
import com.rpm.core.domain.repository.RecipeByIdRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RecipeByIdUseCaseTest {
  private val mockRepository = mockk<RecipeByIdRepository>(relaxed = true)
  private val subject by lazy { RecipeByIdUseCase(mockRepository) }

  @Test
  fun `invoke when repository returns success then should emit success`() = runTest {
    // GIVEN
    val mockList = listOf(
      Recipe(
        id = FAKE_ID,
        meal = FAKE_DESCRIPTION,
        category = FAKE_CATEGORY,
        instructions = FAKE_INSTRUCTIONS,
        thumb = FAKE_THUMB,
        youtubeLink = FAKE_YOUTUBE_LINK,
        ingredients = emptyList(),
        source = FAKE_SOURCE,
      )
    )
    coEvery { mockRepository.getRecipeBy(FAKE_CATEGORY) } returns flowOf(mockList)

    // WHEN
    val resultFlow = subject.invoke(FAKE_CATEGORY)

    // THEN
    assertEquals(Result.success(mockList), resultFlow.single())
  }

  @Test
  fun `invoke when repository throws exception then should emit failure`() = runTest {
    // GIVEN
    val sutException = Exception("Error")
    coEvery { mockRepository.getRecipeBy(FAKE_CATEGORY) } throws sutException

    // WHEN
    val resultFlow = subject.invoke(FAKE_CATEGORY)

    // THEN
    assertEquals(Result.failure<Exception>(sutException), resultFlow.single())
  }

  companion object Companion {
    private const val FAKE_ID = "1"
    private const val FAKE_THUMB = "fake_thumb"
    private const val FAKE_CATEGORY = "fake_category"
    private const val FAKE_DESCRIPTION = "fake_description"
    private const val FAKE_YOUTUBE_LINK = "fake_youtube_link"
    private const val FAKE_SOURCE = "fake_source"
    private const val FAKE_INSTRUCTIONS = "fake_instructions"
  }
}
