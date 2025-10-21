package com.rpm.recipe.by.id.viewmodel

import com.rpm.core.domain.entity.Meal
import com.rpm.core.domain.entity.Recipe
import com.rpm.core.domain.entity.RecipeResponse
import com.rpm.recipe.by.id.repository.RecipeByIdRepositoryImplTest
import com.rpm.recipe.by.id.state.RecipeByIdUiAction
import com.rpm.recipe.by.id.state.RecipeByIdUiEffect
import com.rpm.recipe.by.id.usecase.RecipeByIdUseCase
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
class RecipeByIdViewModelTest {
  private val mockUseCase = mockk<RecipeByIdUseCase>(relaxed = true)
  private val subject by lazy { RecipeByIdViewModel(mockUseCase) }

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
  fun `handleAction when NavigateBack THEN emits NavigateBack`() = runTest {
    // GIVEN
    val effects = mutableListOf<RecipeByIdUiEffect>()
    val job = launch { subject.uiEffect.toList(effects) }

    // WHEN
    subject.handleAction(RecipeByIdUiAction.NavigateBack)
    testDispatcher.scheduler.advanceUntilIdle()

    // THEN
    assertEquals(1, effects.size)
    assertEquals(RecipeByIdUiEffect.NavigateBack, effects.first())

    // CANCEL JOB TO AVOID MEMORY LEAKS
    job.cancel()
  }

  @Test
  fun `handleAction when LoadData and success THEN uiState updated with meals`() = runTest {
    // GIVEN
    val fakeRecipe = listOf(
      Recipe(
        id = FAKE_ID,
        meal = FAKE_DESCRIPTION,
        category = FAKE_CATEGORY,
        instructions = FAKE_INSTRUCTIONS,
        thumb = FAKE_THUMB,
        youtubeLink = FAKE_YOUTUBE_LINK,
        ingredients = emptyList(),
        source = FAKE_SOURCE,
      ),
    )

    coEvery { mockUseCase.invoke(FAKE_CATEGORY) } returns flow {
      emit(Result.success(fakeRecipe))
    }

    // WHEN
    subject.handleAction(RecipeByIdUiAction.LoadData(FAKE_CATEGORY))
    testDispatcher.scheduler.advanceUntilIdle()

    // THEN
    val state = subject.uiState.value
    assertEquals(false, state.isLoading)
    assertEquals(fakeRecipe, state.recipes)
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
    subject.handleAction(RecipeByIdUiAction.LoadData(FAKE_CATEGORY))
    testDispatcher.scheduler.advanceUntilIdle()

    // THEN
    val state = subject.uiState.value
    assertEquals(false, state.isLoading)
    assertTrue(state.error!!.contains("Network error"))

    coVerify(exactly = 1) { mockUseCase.invoke(FAKE_CATEGORY) }
  }

  companion object Companion {
    private const val FAKE_THUMB = "fake_thumb"
    private const val FAKE_CATEGORY = "fake_category"
    private const val FAKE_DESCRIPTION = "fake_description"
    private const val FAKE_ID = "1"
    private const val FAKE_YOUTUBE_LINK = "fake_youtube_link"
    private const val FAKE_SOURCE = "fake_source"
    private const val FAKE_INSTRUCTIONS = "fake_instructions"
  }
}
