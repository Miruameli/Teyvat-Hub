package com.teyvat.hub.presentation.roster

import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teyvat.hub.application.CharacterRepository
import com.teyvat.hub.domain.character.Character
import dagger.hilt.android.lifecycle.HiltViewModel

/** Roster list state: filtered characters plus search text. */
data class RosterUiState(
    val characters: List<Character> = emptyList(),
    val query: String = "",
    val onlyFavorites: Boolean = false,
)

/**
 * Drives the character roster screen.
 *
 * Keeps only the query in this class; all content lives in the repository so
 * the same flow survives configuration changes and process death.
 */
@HiltViewModel
class RosterViewModel @Inject constructor(
    private val repository: CharacterRepository,
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val onlyFavorites = MutableStateFlow(false)

    val uiState: StateFlow<RosterUiState> = combine(
        repository.observeRoster(),
        repository.observeFavorites(),
        query,
        onlyFavorites,
    ) { all, favorites, searchQuery, showFavorites ->
        val base = if (showFavorites) favorites else all
        RosterUiState(
            characters = filterByQuery(base, searchQuery),
            query = searchQuery,
            onlyFavorites = showFavorites,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RosterUiState(),
    )

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun onToggleFavoritesOnly(value: Boolean) {
        onlyFavorites.value = value
    }

    fun onToggleFavorite(character: Character) {
        viewModelScope.launch { repository.toggleFavorite(character.id) }
    }

    private fun filterByQuery(
        characters: List<Character>,
        searchQuery: String,
    ): List<Character> {
        if (searchQuery.isBlank()) return characters
        val needle = searchQuery.trim().lowercase()
        return characters.filter { it.name.lowercase().contains(needle) }
    }
}
