package com.teyvat.hub.presentation.event

import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teyvat.hub.application.EventRepository
import com.teyvat.hub.domain.event.EventKind
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * Drives the event and banner calendar.
 *
 * Lifecycle is evaluated inside the flow using an injectable clock, so an event
 * that ends while the app is open moves out of the active section on the next
 * emission without any background refresh.
 */
@HiltViewModel
class EventCalendarViewModel @Inject constructor(
    private val repository: EventRepository,
    @Named("timeProvider") private val timeProvider: () -> Long,
) : ViewModel() {

    private val kindFilter = MutableStateFlow<EventKind?>(null)

    val uiState: StateFlow<EventCalendarUiState> = combine(
        repository.observeAll(),
        kindFilter,
    ) { events, kind ->
        buildEventCalendarState(events, timeProvider(), kind)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EventCalendarUiState(),
    )

    /** Cycles the filter through all rows, events only, then banners only. */
    fun onCycleKindFilter() {
        val next = when (kindFilter.value) {
            null -> EventKind.EVENT
            EventKind.EVENT -> EventKind.BANNER
            EventKind.BANNER -> null
        }
        kindFilter.value = next
    }
}
