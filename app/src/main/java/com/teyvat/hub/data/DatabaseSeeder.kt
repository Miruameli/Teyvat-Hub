package com.teyvat.hub.data

import javax.inject.Inject
import javax.inject.Singleton
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import com.teyvat.hub.data.content.AssetContentLoader
import com.teyvat.hub.data.content.seeding.ChecklistSeedContent
import com.teyvat.hub.data.content.seeding.EventSeedContent
import com.teyvat.hub.data.content.seeding.TeamSeedContent
import com.teyvat.hub.data.local.character.CharacterDao
import com.teyvat.hub.data.local.checklist.ChecklistDao
import com.teyvat.hub.data.local.event.GameEventDao
import com.teyvat.hub.data.local.team.TeamDao
import com.teyvat.hub.data.local.weapon.WeaponDao
import com.teyvat.hub.data.mapper.toEntity
import com.teyvat.hub.data.mapper.toRegionNames

/**
 * Seeds offline content on first launch, and keeps it current afterwards.
 *
 * The app is offline-first, so the roster, checklist and events must exist in
 * local storage before any screen renders. Checklist rows are written once
 * because they carry user state and the bundle never renames them; roster and
 * weapon rows are written on every launch, but only those the bundle adds, so
 * a content update reaches an existing install without a schema bump and
 * without touching a row a player already owns. Event rows are refreshed on
 * every launch because their windows are relative to the current date.
 *
 * Roster and weapons come from the bundled JSON rather than from code, so a
 * content update is a bundle diff instead of a compile.
 */
@Singleton
class DatabaseSeeder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val characterDao: CharacterDao,
    private val weaponDao: WeaponDao,
    private val checklistDao: ChecklistDao,
    private val eventDao: GameEventDao,
    private val teamDao: TeamDao,
) {

    private val loader = AssetContentLoader(context)

    suspend fun seedIfEmpty() {
        seedRosterFromBundle()
        seedWeaponsFromBundle()
        seedChecklistIfEmpty()
        seedEvents()
        seedTeamTemplates()
    }

    /**
     * Writes the bundled roster, adding rows an earlier build did not have.
     *
     * An install that shipped before a bundle update already has rows, so
     * seeding stops there — but only for the rows it already owns. Rows that
     * did not exist yet are still written, which is what lets a bundle update
     * reach an existing install without a schema bump. Upsert is what keeps
     * user state safe: it replaces the row for matching ids only, and the
     * bundle never names an id a player has already favourited.
     *
     * Rows whose keys this build does not know are dropped rather than
     * written with a fallback key, because a character shown under the wrong
     * element is a worse defect than a character that is absent.
     */
    private suspend fun seedRosterFromBundle() {
        val knownIds = characterDao.findAllIds().toSet()
        val regionNames = loader.regions().toRegionNames()
        val characters = loader.characters().mapNotNull { it.toEntity(regionNames) }
        characterDao.upsertAll(characters.filterNot { it.id in knownIds })
    }

    /** Same rule as the roster: keep existing rows, add what is missing. */
    private suspend fun seedWeaponsFromBundle() {
        val knownIds = weaponDao.findAllIds().toSet()
        val weapons = loader.weapons().mapNotNull { it.toEntity() }
        weaponDao.upsertAll(weapons.filterNot { it.id in knownIds })
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
