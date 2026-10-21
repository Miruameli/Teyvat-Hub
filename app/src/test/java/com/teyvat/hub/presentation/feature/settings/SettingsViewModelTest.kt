package com.teyvat.hub.presentation.feature.settings

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import com.teyvat.hub.application.language.AppLanguage
import com.teyvat.hub.application.language.LanguagePreference
import com.teyvat.hub.application.language.LocaleApplier
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Verifies how the settings screen decides which language to show.
 *
 * The platform is asked first, because a player can also change the language
 * from the system settings. The stored preference is only the fallback.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val preference = mockk<LanguagePreference>()
    private val localeApplier = mockk<LocaleApplier>()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `the platform locale wins over the stored preference`() = runTest {
        every { preference.read() } returns AppLanguage.INDONESIAN
        every { localeApplier.current(AppLanguage.INDONESIAN) } returns AppLanguage.ENGLISH

        val viewModel = viewModel()
        advanceUntilIdle()

        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.language)
    }

    @Test
    fun `the stored preference is used when the platform has no locale`() = runTest {
        every { preference.read() } returns AppLanguage.INDONESIAN
        every { localeApplier.current(AppLanguage.INDONESIAN) } returns AppLanguage.INDONESIAN

        val viewModel = viewModel()
        advanceUntilIdle()

        assertEquals(AppLanguage.INDONESIAN, viewModel.uiState.value.language)
    }

    @Test
    fun `choosing a language stores it and applies it`() = runTest {
        every { preference.read() } returns AppLanguage.INDONESIAN
        every { localeApplier.current(AppLanguage.INDONESIAN) } returns AppLanguage.INDONESIAN
        coEvery { preference.write(any()) } returns Unit
        every { localeApplier.apply(any()) } returns Unit

        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onLanguageSelected(AppLanguage.ENGLISH)
        advanceUntilIdle()

        coVerify { preference.write(AppLanguage.ENGLISH) }
        verify { localeApplier.apply(AppLanguage.ENGLISH) }
        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.language)
    }

    private fun viewModel() = SettingsViewModel(
        preference = preference,
        localeApplier = localeApplier,
    )
}
