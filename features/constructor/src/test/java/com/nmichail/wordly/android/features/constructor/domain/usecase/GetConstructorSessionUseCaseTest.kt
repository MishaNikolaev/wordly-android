package com.nmichail.wordly.android.features.constructor.domain.usecase

import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorPhrase
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorSession
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorWord
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
class GetConstructorSessionUseCaseTest {

	private val constructorRepository: ConstructorRepository = mock()
	private val getConstructorSessionUseCase = GetConstructorSessionUseCase(constructorRepository)

	private val session = ConstructorSession(
		themeId = "philosophy",
		themeTitle = "Философские мысли",
		phrases = listOf(
			ConstructorPhrase(
				id = "socrates-1",
				question = "Я знаю, что ничего не знаю.",
				author = "Сократ",
				words = listOf(
					ConstructorWord(id = "w1", text = "I"),
					ConstructorWord(id = "w2", text = "know"),
					ConstructorWord(id = "w3", text = "nothing"),
				),
				correctOrder = listOf("w1", "w2", "w3"),
			),
		),
	)

	@Test
	fun `invoke EXPECT repository get session`() = runTest {
		whenever(constructorRepository.getSession("philosophy")) doReturn session

		getConstructorSessionUseCase("philosophy")

		verify(constructorRepository).getSession("philosophy")
	}

	@Test
	fun `invoke EXPECT session from repository`() = runTest {
		whenever(constructorRepository.getSession("philosophy")) doReturn session

		val actual = getConstructorSessionUseCase("philosophy")

		assertEquals(session, actual)
	}
}