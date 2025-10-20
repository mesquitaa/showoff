package com.rpm.category.list.repository

import com.rpm.category.list.api.CategoryListApi
import com.rpm.core.domain.entity.Category
import com.rpm.core.domain.entity.CategoryListResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CategoryListRepositoryTest {

  private val mockApi = mockk<CategoryListApi>(relaxed = true)

  private val subject by lazy {
    CategoryListRepository(
      api = mockApi,
    )
  }

  @Test
  fun `searchMovies returns movies from api`() = runTest {
    // GIVEN
    coEvery { mockApi.getCategories() } returns CategoryListResponse(
      categories = listOf(
        Category(
          id = FAKE_ID,
          thumb = FAKE_THUMB,
          category = FAKE_CATEGORY,
          description = FAKE_DESCRIPTION,
        )
      ),
    )

    // WHEN
    val result = subject.getCategories().single()

    // THEN
    coVerify { mockApi.getCategories() }
    assertEquals(1, result.size)
    assertEquals(FAKE_ID, result[0].id)
    assertEquals(FAKE_THUMB, result[0].thumb)
    assertEquals(FAKE_CATEGORY, result[0].category)
    assertEquals(FAKE_DESCRIPTION, result[0].description)
  }

  companion object {
    private const val FAKE_ID = 1
    private const val FAKE_THUMB = "fake_thumb"
    private const val FAKE_CATEGORY = "fake_category"
    private const val FAKE_DESCRIPTION = "fake_description"
  }
}
