package com.rpm.recipe.by.categories.viewmodel

import com.rpm.core.domain.entity.Meal
import com.rpm.recipe.by.categories.state.MealsByCategoryUiAction
import com.rpm.recipe.by.categories.state.MealsByCategoryUiEffect
import com.rpm.recipe.by.categories.usecase.MealsByCategoryUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MealsByCategoryViewModelTest {
  private val mockUseCase = mockk<MealsByCategoryUseCase>(relaxed = true)
  private val subject by lazy { MealsByCategoryViewModel(mockUseCase) }

  private val testDispatcher = StandardTestDispatcher()

  @BeforeEach
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
  }

  @AfterEach
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `handleAction when SelectMeal THEN uiState is updated with null error`() = runTest {
    // GIVEN
    val sutMeal = Meal(
      id = FAKE_ID,
      thumb = FAKE_THUMB,
      meal = FAKE_DESCRIPTION,
    )

    val effects = mutableListOf<MealsByCategoryUiEffect>()
    val job = launch { subject.uiEffect.toList(effects) }

    // WHEN
    subject.handleAction(MealsByCategoryUiAction.SelectMeal(sutMeal))
    testDispatcher.scheduler.advanceUntilIdle()

    // THEN
    assertEquals(1, effects.size)
    assertEquals(MealsByCategoryUiEffect.NavigateToRecipeDetails(sutMeal.id), effects.first())

    // CANCEL THE JOB TO AVOID MEMORY LEAKS
    job.cancel()
  }

  @Test
  fun `handleAction when SelectMeal THEN emits NavigateToRecipeDetails`() = runTest {
    // GIVEN
    val sutMeal = Meal(
      id = FAKE_ID,
      thumb = FAKE_THUMB,
      meal = FAKE_DESCRIPTION,
    )

    val effects = mutableListOf<MealsByCategoryUiEffect>()
    val job = launch { subject.uiEffect.toList(effects) }

    // WHEN
    subject.handleAction(MealsByCategoryUiAction.SelectMeal(sutMeal))
    testDispatcher.scheduler.advanceUntilIdle()

    // THEN
    assertEquals(1, effects.size)
    assertEquals(MealsByCategoryUiEffect.NavigateToRecipeDetails(sutMeal.id), effects.first())

    // CANCEL JOB TO AVOID MEMORY LEAKS
    job.cancel()
  }

  @Test
  fun `handleAction when NavigateBack THEN emits NavigateBack`() = runTest {
    // GIVEN
    val effects = mutableListOf<MealsByCategoryUiEffect>()
    val job = launch { subject.uiEffect.toList(effects) }

    // WHEN
    subject.handleAction(MealsByCategoryUiAction.NavigateBack)
    testDispatcher.scheduler.advanceUntilIdle()

    // THEN
    assertEquals(1, effects.size)
    assertEquals(MealsByCategoryUiEffect.NavigateBack, effects.first())

    // CANCEL JOB TO AVOID MEMORY LEAKS
    job.cancel()
  }

  @Test
  fun `handleAction when LoadData and success THEN uiState updated with meals`() = runTest {
    // GIVEN
    val fakeMeals = listOf(
      Meal(id = 1, thumb = "thumb1", meal = "descriptionA"),
      Meal(id = 2, thumb = "thumb2", meal = "descriptionB"),
    )

    coEvery { mockUseCase.invoke(FAKE_CATEGORY) } returns flow {
      emit(Result.success(fakeMeals))
    }

    // WHEN
    subject.handleAction(MealsByCategoryUiAction.LoadData(FAKE_CATEGORY))
    testDispatcher.scheduler.advanceUntilIdle()

    // THEN
    val state = subject.uiState.value
    assertEquals(false, state.isLoading)
    assertEquals(fakeMeals, state.meals)
    assertEquals(null, state.error)

    coVerify(exactly = 1) { mockUseCase.invoke(FAKE_CATEGORY) }
  }

  @Test
  fun `handleAction when LoadData and failure THEN uiState updated with error`() = runTest {
    // GIVEN
    val exception = RuntimeException("Network error")
    coEvery { mockUseCase.invoke(FAKE_CATEGORY) } returns flow {
      emit(Result.failure(exception))
    }

    // WHEN
    subject.handleAction(MealsByCategoryUiAction.LoadData(FAKE_CATEGORY))
    testDispatcher.scheduler.advanceUntilIdle()

    // THEN
    val state = subject.uiState.value
    assertEquals(false, state.isLoading)
    assertTrue(state.error!!.contains("Network error"))

    coVerify(exactly = 1) { mockUseCase.invoke(FAKE_CATEGORY) }
  }

  companion object Companion {
    private const val FAKE_ID = 1
    private const val FAKE_THUMB = "fake_thumb"
    private const val FAKE_CATEGORY = "fake_category"
    private const val FAKE_DESCRIPTION = "fake_description"
  }
}
