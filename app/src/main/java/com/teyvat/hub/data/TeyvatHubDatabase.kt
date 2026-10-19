package com.teyvat.hub.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.teyvat.hub.data.local.character.CharacterDao
import com.teyvat.hub.data.local.character.CharacterEntity
import com.teyvat.hub.data.local.checklist.ChecklistDao
import com.teyvat.hub.data.local.checklist.ChecklistItemEntity
import com.teyvat.hub.data.local.event.GameEventDao
import com.teyvat.hub.data.local.event.GameEventEntity
import com.teyvat.hub.data.local.team.TeamDao
import com.teyvat.hub.data.local.team.TeamEntity
import com.teyvat.hub.data.local.weapon.WeaponDao
import com.teyvat.hub.data.local.weapon.WeaponEntity

/**
 * Local database holding every offline content table.
 *
 * Content is seeded once on first launch by a Room callback so the app stays
 * fully offline. Bump [version] with a matching migration whenever a schema
 * change ships, never by wiping user data.
 */
@Database(
    entities = [
        CharacterEntity::class,
        WeaponEntity::class,
        TeamEntity::class,
        ChecklistItemEntity::class,
        GameEventEntity::class,
    ],
    version = 1,
    // Schema export is enabled once v1 ships so migrations can be verified
    // from version control; kept off until the first migration appears.
    exportSchema = false,
)
abstract class TeyvatHubDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun weaponDao(): WeaponDao

    abstract fun teamDao(): TeamDao

    abstract fun checklistDao(): ChecklistDao

    abstract fun eventDao(): GameEventDao

    companion object {
        const val NAME = "teyvat-hub.db"
    }
}
