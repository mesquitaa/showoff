package com.rpm.category.list.viewmodel

import com.rpm.category.list.state.CategoryListUiAction
import com.rpm.category.list.state.CategoryListUiEffect
import com.rpm.category.list.usecase.CategoryListUseCase
import com.rpm.core.domain.entity.Category
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
class CategoryListViewModelTest {
  private val mockUseCase = mockk<CategoryListUseCase>(relaxed = true)
  private val subject by lazy { CategoryListViewModel(mockUseCase) }

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
  fun `handleAction when SelectCategory THEN uiState is updated with null error`() = runTest {
    // GIVEN
    val sutCategory = Category(
      id = FAKE_ID,
      thumb = FAKE_THUMB,
      category = FAKE_CATEGORY,
      description = FAKE_DESCRIPTION,
    )

    val effects = mutableListOf<CategoryListUiEffect>()
    val job = launch { subject.uiEffect.toList(effects) }

    // WHEN
    subject.handleAction(CategoryListUiAction.SelectCategory(sutCategory))
    testDispatcher.scheduler.advanceUntilIdle()

    // THEN
    assertEquals(1, effects.size)
    assertEquals(CategoryListUiEffect.NavigateToCategoryList(FAKE_CATEGORY), effects.first())

    // CANCEL THE JOB TO AVOID MEMORY LEAKS
    job.cancel()
  }

  @Test
  fun `handleAction when SelectCategory THEN emits NavigateToCategoryList`() = runTest {
    // GIVEN
    val sutCategory = Category(
      id = FAKE_ID,
      thumb = FAKE_THUMB,
      category = FAKE_CATEGORY,
      description = FAKE_DESCRIPTION,
    )

    val effects = mutableListOf<CategoryListUiEffect>()
    val job = launch { subject.uiEffect.toList(effects) }

    // WHEN
    subject.handleAction(CategoryListUiAction.SelectCategory(sutCategory))
    testDispatcher.scheduler.advanceUntilIdle()

    // THEN
    assertEquals(1, effects.size)
    assertEquals(CategoryListUiEffect.NavigateToCategoryList(FAKE_CATEGORY), effects.first())

    // CANCEL JOB TO AVOID MEMORY LEAKS
    job.cancel()
  }

  @Test
  fun `handleAction when LoadData and success THEN uiState updated with categories`() = runTest {
    // GIVEN
    val fakeCategories = listOf(
      Category(id = 1, thumb = "thumb1", category = "A", description = "descriptionA"),
      Category(id = 2, thumb = "thumb2", category = "B", description = "descriptionB"),
    )

    coEvery { mockUseCase.invoke() } returns flow {
      emit(Result.success(fakeCategories))
    }

    // WHEN
    subject.handleAction(CategoryListUiAction.LoadData)
    testDispatcher.scheduler.advanceUntilIdle()

    // THEN
    val state = subject.uiState.value
    assertEquals(false, state.isLoading)
    assertEquals(fakeCategories, state.categories)
    assertEquals(null, state.error)

    coVerify(exactly = 1) { mockUseCase.invoke() }
  }

  @Test
  fun `handleAction when LoadData and failure THEN uiState updated with error`() = runTest {
    // GIVEN
    val exception = RuntimeException("Network error")
    coEvery { mockUseCase.invoke() } returns flow {
      emit(Result.failure(exception))
    }

    // WHEN
    subject.handleAction(CategoryListUiAction.LoadData)
    testDispatcher.scheduler.advanceUntilIdle()

    // THEN
    val state = subject.uiState.value
    assertEquals(false, state.isLoading)
    assertTrue(state.error!!.contains("Network error"))

    coVerify(exactly = 1) { mockUseCase.invoke() }
  }

  companion object {
    private const val FAKE_ID = 1
    private const val FAKE_THUMB = "fake_thumb"
    private const val FAKE_CATEGORY = "fake_category"
    private const val FAKE_DESCRIPTION = "fake_description"
  }
}
