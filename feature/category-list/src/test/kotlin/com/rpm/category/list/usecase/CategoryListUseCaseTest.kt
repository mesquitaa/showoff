package com.rpm.category.list.usecase

import com.rpm.core.domain.entity.Category
import com.rpm.core.domain.repository.MealCategoryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CategoryListUseCaseTest {
  private val mockRepository = mockk<MealCategoryRepository>(relaxed = true)
  private val subject by lazy { CategoryListUseCase(mockRepository) }

  @Test
  fun `invoke when repository returns success then should emit success`() = runTest {
    // GIVEN
    val mockList = listOf(Category(1, "Category", "url", "description"))
    coEvery { mockRepository.getCategories() } returns flowOf(mockList)

    // WHEN
    val resultFlow = subject.invoke()

    // THEN
    assertEquals(Result.success(mockList), resultFlow.single())
  }

  @Test
  fun `invoke when repository throws exception then should emit failure`() = runTest {
    // GIVEN
    val sutException = Exception("Error")
    coEvery { mockRepository.getCategories() } throws sutException

    // WHEN
    val resultFlow = subject.invoke()

    // THEN
    assertEquals(Result.failure<Exception>(sutException), resultFlow.single())
  }
}
