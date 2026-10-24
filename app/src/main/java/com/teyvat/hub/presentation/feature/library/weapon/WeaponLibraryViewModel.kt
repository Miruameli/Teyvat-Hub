package com.teyvat.hub.presentation.feature.library.weapon

import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teyvat.hub.application.repository.WeaponRepository
import com.teyvat.hub.domain.character.WeaponType
import com.teyvat.hub.domain.equipment.Weapon
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * State shown by the weapon library.
 *
 * A null type filter means every family, which is the only state a screen can
 * start in: a player who has not chosen a family has not asked for one.
 */
data class WeaponLibraryUiState(
    val weapons: List<Weapon> = emptyList(),
    val typeFilter: WeaponType? = null,
    val types: List<WeaponType> = WeaponType.entries,
)

/**
 * Drives the weapon library screen, seeded from the bundled JSON.
 *
 * Filtering is a repository concern rather than a screen concern, so the list
 * is re-read with the chosen type instead of being narrowed in place. That
 * costs one query and keeps a single source for what "of this type" means.
 */
@HiltViewModel
class WeaponLibraryViewModel @Inject constructor(
    private val repository: WeaponRepository,
) : ViewModel() {

    private val typeFilter = MutableStateFlow<WeaponType?>(null)

    val uiState: StateFlow<WeaponLibraryUiState> = combine(
        repository.observeAll(),
        typeFilter,
    ) { all, selected ->
        val weapons = if (selected == null) all else all.filter { it.type == selected }
        WeaponLibraryUiState(weapons = weapons, typeFilter = selected)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WeaponLibraryUiState(),
    )

    /** Tapping the active family clears it, which is the rule the roster uses. */
    fun onTypeSelected(type: WeaponType) {
        typeFilter.value = if (typeFilter.value == type) null else type
    }
}