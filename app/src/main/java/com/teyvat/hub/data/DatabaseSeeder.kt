package com.teyvat.hub.data

import javax.inject.Inject
import javax.inject.Singleton
import com.teyvat.hub.data.local.character.CharacterDao
import com.teyvat.hub.data.local.checklist.ChecklistDao
import com.teyvat.hub.data.local.event.GameEventDao
import com.teyvat.hub.data.local.team.TeamDao
import com.teyvat.hub.data.seeding.CharacterSeedContent
import com.teyvat.hub.data.seeding.ChecklistSeedContent
import com.teyvat.hub.data.seeding.EventSeedContent
import com.teyvat.hub.data.seeding.TeamSeedContent

/**
 * Seeds offline content on first launch.
 *
 * The app is offline-first, so the roster, checklist and events must exist in
 * local storage before any screen renders. Character and checklist rows are
 * written once because they carry user state; event rows are refreshed on
 * every launch because their windows are relative to the current date.
 */
@Singleton
class DatabaseSeeder @Inject constructor(
    private val characterDao: CharacterDao,
    private val checklistDao: ChecklistDao,
    private val eventDao: GameEventDao,
    private val teamDao: TeamDao,
) {

    suspend fun seedIfEmpty() {
        seedCharactersIfEmpty()
        seedChecklistIfEmpty()
        seedEvents()
        seedTeamTemplates()
    }

    private suspend fun seedCharactersIfEmpty() {
        if (characterDao.countRows() > 0) return
        characterDao.upsertAll(CharacterSeedContent.all())
    }

    private suspend fun seedChecklistIfEmpty() {
        if (checklistDao.count() > 0) return
        checklistDao.upsertAll(ChecklistSeedContent.all())
    }

    /**
     * Refreshes the event rows on every launch.
     *
     * Events carry no user state, unlike checklist items, so overwriting them
     * is safe. The refresh is what keeps the calendar populated: a one-shot
     * seed would leave every row in the past as time passes, and a fresh
     * install would inherit whatever date the rows were created with.
     */
    private suspend fun seedEvents() {
        eventDao.upsertAll(EventSeedContent.all(System.currentTimeMillis()))
    }

    /**
     * Writes the built-in team templates.
     *
     * Templates carry no user state, so refreshing them on every launch is
     * safe and keeps them in step with the character roster.
     */
    private suspend fun seedTeamTemplates() {
        teamDao.upsertAll(TeamSeedContent.all())
    }
}
