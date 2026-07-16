package com.nmichail.wordly.android.features.dev.networkselection.presentation

import com.nmichail.wordly.android.core.preferences.domain.usecase.ClearAuthTokensUseCase
import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestComponentContext
import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand
import com.nmichail.wordly.android.features.dev.networkselection.domain.usecase.GetNetworkStandsUseCase
import com.nmichail.wordly.android.features.dev.networkselection.domain.usecase.GetSelectedNetworkStandUseCase
import com.nmichail.wordly.android.features.dev.networkselection.domain.usecase.SetNetworkStandUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(
	MockitoExtension::class,
	TestCoroutineExtension::class,
	InstantExecutorExtension::class,
)
class DefaultNetworkSelectionComponentTest {

	private val stands = listOf(NetworkStand.DEV, NetworkStand.MOCK)

	private val getNetworkStandsUseCase: GetNetworkStandsUseCase = mock()
	private val getSelectedNetworkStandUseCase: GetSelectedNetworkStandUseCase = mock()
	private val setNetworkStandUseCase: SetNetworkStandUseCase = mock()
	private val clearAuthTokensUseCase: ClearAuthTokensUseCase = mock()
	private val networkSelectionRouter: NetworkSelectionRouter = mock()
	private val onFinished: () -> Unit = mock()

	private lateinit var component: DefaultNetworkSelectionComponent
	private val model get() = component.model.value

	@BeforeEach
	fun setUp() {
		whenever(getNetworkStandsUseCase()) doReturn stands
		whenever(getSelectedNetworkStandUseCase()) doReturn NetworkStand.DEV
		component = DefaultNetworkSelectionComponent(
			componentContext = createTestComponentContext(),
			networkSelectionStoreFactory = NetworkSelectionStoreFactory(
				getNetworkStandsUseCase = getNetworkStandsUseCase,
				getSelectedNetworkStandUseCase = getSelectedNetworkStandUseCase,
				setNetworkStandUseCase = setNetworkStandUseCase,
				clearAuthTokensUseCase = clearAuthTokensUseCase,
			),
			onFinished = onFinished,
			networkSelectionRouter = networkSelectionRouter,
		)
	}

	@Test
	fun `init EXPECT init state with data`() {
		val expected = NetworkSelectionComponent.State(
			stands = stands,
			selectedStand = NetworkStand.DEV,
		)

		assertEquals(expected, model)
	}

	@Test
	fun `select new stand EXPECT set stand`() = runTest {
		component.handleSelectStand(NetworkStand.MOCK)

		verify(setNetworkStandUseCase).invoke(NetworkStand.MOCK)
	}

	@Test
	fun `select new stand EXPECT clear tokens`() = runTest {
		component.handleSelectStand(NetworkStand.MOCK)

		verify(clearAuthTokensUseCase).invoke()
	}

	@Test
	fun `select new stand EXPECT state with new stand`() = runTest {
		val expected = NetworkSelectionComponent.State(
			stands = stands,
			selectedStand = NetworkStand.MOCK,
		)

		component.handleSelectStand(NetworkStand.MOCK)

		assertEquals(expected, model)
	}

	@Test
	fun `select new stand EXPECT restart app`() = runTest {
		component.handleSelectStand(NetworkStand.MOCK)

		verify(networkSelectionRouter).restartApp()
	}

	@Test
	fun `select same stand EXPECT set stand not invoked`() = runTest {
		component.handleSelectStand(NetworkStand.DEV)

		verify(setNetworkStandUseCase, never()).invoke(NetworkStand.DEV)
	}

	@Test
	fun `select same stand EXPECT clear tokens not invoked`() = runTest {
		component.handleSelectStand(NetworkStand.DEV)

		verify(clearAuthTokensUseCase, never()).invoke()
	}

	@Test
	fun `select same stand EXPECT restart app not invoked`() = runTest {
		component.handleSelectStand(NetworkStand.DEV)

		verify(networkSelectionRouter, never()).restartApp()
	}

	@Test
	fun `navigate back EXPECT finished`() {
		component.handleNavigateBack()

		verify(onFinished).invoke()
	}
}
