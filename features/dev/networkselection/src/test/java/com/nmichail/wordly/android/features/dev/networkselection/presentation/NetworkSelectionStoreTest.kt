package com.nmichail.wordly.android.features.dev.networkselection.presentation

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.preferences.domain.usecase.ClearAuthTokensUseCase
import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestLifecycle
import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand
import com.nmichail.wordly.android.features.dev.networkselection.domain.usecase.GetNetworkStandsUseCase
import com.nmichail.wordly.android.features.dev.networkselection.domain.usecase.GetSelectedNetworkStandUseCase
import com.nmichail.wordly.android.features.dev.networkselection.domain.usecase.SetNetworkStandUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
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
class NetworkSelectionStoreTest {

	private val stands = listOf(NetworkStand.DEV, NetworkStand.MOCK)

	private val getNetworkStandsUseCase: GetNetworkStandsUseCase = mock()
	private val getSelectedNetworkStandUseCase: GetSelectedNetworkStandUseCase = mock()
	private val setNetworkStandUseCase: SetNetworkStandUseCase = mock()
	private val clearAuthTokensUseCase: ClearAuthTokensUseCase = mock()

	private lateinit var lifecycle: LifecycleRegistry
	private lateinit var store: NetworkSelectionStore

	@BeforeEach
	fun setUp() {
		whenever(getNetworkStandsUseCase()) doReturn stands
		whenever(getSelectedNetworkStandUseCase()) doReturn NetworkStand.DEV
		lifecycle = createTestLifecycle()
		store = createStore()
	}

	@AfterEach
	fun tearDown() {
		lifecycle.destroy()
	}

	@Test
	fun `init EXPECT init state with data`() {
		val expected = NetworkSelectionComponent.State(
			stands = stands,
			selectedStand = NetworkStand.DEV,
		)

		assertEquals(expected, store.state)
	}

	@Test
	fun `select new stand EXPECT set stand`() {
		store.accept(NetworkSelectionStore.Intent.SelectStand(NetworkStand.MOCK))

		verify(setNetworkStandUseCase).invoke(NetworkStand.MOCK)
	}

	@Test
	fun `select new stand EXPECT clear tokens`() {
		store.accept(NetworkSelectionStore.Intent.SelectStand(NetworkStand.MOCK))

		verify(clearAuthTokensUseCase).invoke()
	}

	@Test
	fun `select new stand EXPECT state with new stand`() {
		val expected = NetworkSelectionComponent.State(
			stands = stands,
			selectedStand = NetworkStand.MOCK,
		)

		store.accept(NetworkSelectionStore.Intent.SelectStand(NetworkStand.MOCK))

		assertEquals(expected, store.state)
	}

	@Test
	fun `select new stand EXPECT restart app label`() = runTest {
		val labelsChannel = store.labelsChannel(lifecycle)

		store.accept(NetworkSelectionStore.Intent.SelectStand(NetworkStand.MOCK))

		assertEquals(NetworkSelectionComponent.Label.RestartApp, labelsChannel.receive())
	}

	@Test
	fun `select same stand EXPECT set stand not invoked`() {
		store.accept(NetworkSelectionStore.Intent.SelectStand(NetworkStand.DEV))

		verify(setNetworkStandUseCase, never()).invoke(NetworkStand.DEV)
	}

	@Test
	fun `select same stand EXPECT clear tokens not invoked`() {
		store.accept(NetworkSelectionStore.Intent.SelectStand(NetworkStand.DEV))

		verify(clearAuthTokensUseCase, never()).invoke()
	}

	@Test
	fun `navigate back EXPECT navigate back label`() = runTest {
		val labelsChannel = store.labelsChannel(lifecycle)

		store.accept(NetworkSelectionStore.Intent.NavigateBack)

		assertEquals(NetworkSelectionComponent.Label.NavigateBack, labelsChannel.receive())
	}

	private fun createStore(): NetworkSelectionStore =
		NetworkSelectionStoreFactory(
			getNetworkStandsUseCase = getNetworkStandsUseCase,
			getSelectedNetworkStandUseCase = getSelectedNetworkStandUseCase,
			setNetworkStandUseCase = setNetworkStandUseCase,
			clearAuthTokensUseCase = clearAuthTokensUseCase,
		).create()
}
