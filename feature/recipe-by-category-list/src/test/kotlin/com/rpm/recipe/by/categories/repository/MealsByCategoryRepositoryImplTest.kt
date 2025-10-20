package com.rpm.recipe.by.categories.repository

import com.rpm.core.domain.entity.Meal
import com.rpm.core.domain.entity.MealResponse
import com.rpm.recipe.by.categories.api.MealByCategoryApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MealsByCategoryRepositoryImplTest {
  private val mockApi = mockk<MealByCategoryApi>(relaxed = true)

  private val subject by lazy {
    MealsByCategoryRepositoryImpl(
      api = mockApi,
    )
  }

  @Test
  fun `getMealsByCategory - should return list of meals`() = runTest {
    // GIVEN
    coEvery { mockApi.getMealByCategory(FAKE_CATEGORY) } returns MealResponse(
      meals = listOf(
        Meal(
          id = FAKE_ID,
          thumb = FAKE_THUMB,
          meal = FAKE_DESCRIPTION,
        ),
      ),
    )

    // WHEN
    val result = subject.getMealsByCategory(FAKE_CATEGORY).single()

    // THEN
    coVerify { mockApi.getMealByCategory(FAKE_CATEGORY) }
    assertEquals(1, result.size)
    assertEquals(FAKE_ID, result[0].id)
    assertEquals(FAKE_THUMB, result[0].thumb)
    assertEquals(FAKE_DESCRIPTION, result[0].meal)
  }

  companion object Companion {
    private const val FAKE_ID = 1
    private const val FAKE_THUMB = "fake_thumb"
    private const val FAKE_CATEGORY = "fake_category"
    private const val FAKE_DESCRIPTION = "fake_description"
  }
}
