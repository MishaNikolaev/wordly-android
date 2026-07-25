package com.nmichail.wordly.android.features.constructor.domain.usecase

import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorCatalog
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorLevelBanner
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorSection
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorTheme
import com.nmichail.wordly.android.features.constructor.domain.repository.ConstructorRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class GetConstructorCatalogUseCaseTest {

	private val constructorRepository: ConstructorRepository = mock()
	private val getConstructorCatalogUseCase = GetConstructorCatalogUseCase(constructorRepository)

	private val catalog = ConstructorCatalog(
		title = "Конструктор",
		searchPlaceholder = "Поиск темы",
		levelBanner = ConstructorLevelBanner(
			text = "Ваш уровень / темы подбираются под него",
			levelLabel = "B2",
			levels = listOf("A1", "A2", "B1", "B2", "C1", "C2"),
		),
		sections = listOf(
			ConstructorSection(
				title = "Под ваш уровень · B2",
				items = listOf(
					ConstructorTheme(
						id = "philosophy",
						title = "Философские мысли",
						subtitle = "4 фразы",
						badge = "B2",
						imageUrl = null,
					),
				),
			),
		),
	)

	@Test
	fun `invoke EXPECT repository get catalog`() = runTest {
		whenever(constructorRepository.getCatalog()) doReturn catalog

		getConstructorCatalogUseCase()

		verify(constructorRepository).getCatalog()
	}

	@Test
	fun `invoke EXPECT catalog from repository`() = runTest {
		whenever(constructorRepository.getCatalog()) doReturn catalog

		val actual = getConstructorCatalogUseCase()

		assertEquals(catalog, actual)
	}
}