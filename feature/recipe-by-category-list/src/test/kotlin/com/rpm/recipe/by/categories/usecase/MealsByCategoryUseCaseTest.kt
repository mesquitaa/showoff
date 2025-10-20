package com.rpm.recipe.by.categories.usecase

import com.rpm.core.domain.entity.Meal
import com.rpm.core.domain.repository.MealByCategoryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MealsByCategoryUseCaseTest {
  private val mockRepository = mockk<MealByCategoryRepository>(relaxed = true)
  private val subject by lazy { MealsByCategoryUseCase(mockRepository) }

  @Test
  fun `invoke when repository returns success then should emit success`() = runTest {
    // GIVEN
    val mockList = listOf(Meal(1, FAKE_MEAL, FAKE_THUMB))
    coEvery { mockRepository.getMealsByCategory(FAKE_CATEGORY) } returns flowOf(mockList)

    // WHEN
    val resultFlow = subject.invoke(FAKE_CATEGORY)

    // THEN
    assertEquals(Result.success(mockList), resultFlow.single())
  }

  @Test
  fun `invoke when repository throws exception then should emit failure`() = runTest {
    // GIVEN
    val sutException = Exception("Error")
    coEvery { mockRepository.getMealsByCategory(FAKE_CATEGORY) } throws sutException

    // WHEN
    val resultFlow = subject.invoke(FAKE_CATEGORY)

    // THEN
    assertEquals(Result.failure<Exception>(sutException), resultFlow.single())
  }

  companion object {
    private const val FAKE_CATEGORY = "FAKE CATEGORY"
    private const val FAKE_MEAL = "FAKE MEAL"
    private const val FAKE_THUMB = "FAKE THUMB"
  }
}
