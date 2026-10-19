package com.teyvat.hub.data

import com.teyvat.hub.data.local.character.CharacterDao
import com.teyvat.hub.data.local.checklist.ChecklistDao
import com.teyvat.hub.data.local.event.GameEventDao
import com.teyvat.hub.data.seeding.CharacterSeedContent
import com.teyvat.hub.data.seeding.ChecklistSeedContent
import com.teyvat.hub.data.seeding.EventSeedContent
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Seeds offline content on first launch.
 *
 * The app is offline-first, so the roster, checklist and events must exist in
 * local storage before any screen renders. Seeding is idempotent: the insert
 * uses REPLACE on stable ids, so it is safe to run on every launch.
 */
@Singleton
class DatabaseSeeder @Inject constructor(
    private val characterDao: CharacterDao,
    private val checklistDao: ChecklistDao,
    private val eventDao: GameEventDao,
) {

    suspend fun seedIfEmpty() {
        seedCharactersIfEmpty()
        seedChecklistIfEmpty()
        seedEventsIfEmpty()
    }

    private suspend fun seedCharactersIfEmpty() {
        if (characterDao.countRows() > 0) return
        characterDao.upsertAll(CharacterSeedContent.all())
    }

    private suspend fun seedChecklistIfEmpty() {
        if (checklistDao.count() > 0) return
        checklistDao.upsertAll(ChecklistSeedContent.all())
    }

    private suspend fun seedEventsIfEmpty() {
        if (eventDao.readAnyRow() != null) return
        eventDao.upsertAll(EventSeedContent.all())
    }
}
