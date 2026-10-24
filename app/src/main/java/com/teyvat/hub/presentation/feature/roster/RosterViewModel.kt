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
import com.teyvat.hub.domain.character.Element
import com.teyvat.hub.domain.character.RosterFilter
import com.teyvat.hub.domain.character.matching
import dagger.hilt.android.lifecycle.HiltViewModel

/** Roster list state: filtered characters plus how they are narrowed. */
data class RosterUiState(
    val characters: List<Character> = emptyList(),
    val query: String = "",
    val onlyFavorites: Boolean = false,
    val element: Element? = null,
)

/**
 * Drives the character roster screen.
 *
 * The search goes through the repository so the ranking rules live in one
 * place. Every narrowing is then applied through one domain filter, so a
 * search, an element, and the favourites filter can never disagree about
 * which characters are shown.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RosterViewModel @Inject constructor(
    private val repository: CharacterRepository,
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val element = MutableStateFlow<Element?>(null)
    private val onlyFavorites = MutableStateFlow(false)

    private val characters = combine(query, element, onlyFavorites) { searchQuery, elementFilter, favourites ->
        RosterFilter(searchQuery, elementFilter, favourites)
    }.flatMapLatest { filter ->
        val source = if (filter.query.isBlank()) {
            repository.observeRoster()
        } else {
            repository.search(filter.query)
        }
        source.map { list -> list.matching(filter) }
    }

    val uiState: StateFlow<RosterUiState> = combine(
        characters,
        query,
        element,
        onlyFavorites,
    ) { list, searchQuery, elementFilter, favourites ->
        RosterUiState(
            characters = list,
            query = searchQuery,
            element = elementFilter,
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

    /** Sets the element filter; passing the active element again clears it. */
    fun onElementFilterChange(value: Element?) {
        element.value = if (element.value == value) null else value
    }

    fun onToggleFavoritesOnly(value: Boolean) {
        onlyFavorites.value = value
    }

    fun onToggleFavorite(character: Character) {
        viewModelScope.launch { repository.toggleFavorite(character.id) }
    }
}
