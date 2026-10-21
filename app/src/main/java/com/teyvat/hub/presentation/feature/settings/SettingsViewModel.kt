package com.teyvat.hub.presentation.feature.settings

import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teyvat.hub.application.language.AppLanguage
import com.teyvat.hub.application.language.LanguagePreference
import com.teyvat.hub.application.language.LocaleApplier
import dagger.hilt.android.lifecycle.HiltViewModel

/** Which language the player has chosen. */
data class SettingsUiState(
    val language: AppLanguage = AppLanguage.DEFAULT,
)

/**
 * Drives the settings screen.
 *
 * Only the language is adjustable, because it is the only setting that
 * changes what the app shows. The other entries are information: the version,
 * the data sources, the privacy statement, and the disclaimer.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preference: LanguagePreference,
    private val localeApplier: LocaleApplier,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = SettingsUiState(language = preference.read())
        }
    }

    /** Stores the chosen language and applies it to the running app. */
    fun onLanguageSelected(language: AppLanguage) {
        _uiState.value = SettingsUiState(language = language)
        viewModelScope.launch {
            preference.write(language)
            localeApplier.apply(language)
        }
    }
}
