package com.teyvat.hub.presentation.feature.roster

import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teyvat.hub.application.repository.CharacterRepository
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
 * The search goes through the repository, so the ranking rules live in one
 * place instead of in the screen. The list and the favourites filter are
 * resolved in a single flow, so a search and a favourites filter can never
 * disagree about which characters are shown.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RosterViewModel @Inject constructor(
    private val repository: CharacterRepository,
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val onlyFavorites = MutableStateFlow(false)

    private val characters = combine(query, onlyFavorites) { searchQuery, favourites ->
        searchQuery to favourites
    }.flatMapLatest { (searchQuery, favourites) ->
        val source = if (searchQuery.isBlank()) {
            repository.observeRoster()
        } else {
            repository.search(searchQuery)
        }
        if (favourites) {
            source.map { list -> list.filter { it.isFavorite } }
        } else {
            source
        }
    }

    val uiState: StateFlow<RosterUiState> = combine(
        characters,
        query,
        onlyFavorites,
    ) { list, searchQuery, favourites ->
        RosterUiState(
            characters = list,
            query = searchQuery,
            onlyFavorites = favourites,
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
}
