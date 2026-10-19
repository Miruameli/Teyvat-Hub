package com.teyvat.hub.injection

import com.teyvat.hub.data.TeyvatHubDatabase
import com.teyvat.hub.data.local.character.CharacterDao
import com.teyvat.hub.data.local.checklist.ChecklistDao
import com.teyvat.hub.data.local.event.GameEventDao
import com.teyvat.hub.data.local.team.TeamDao
import com.teyvat.hub.data.local.weapon.WeaponDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Exposes one DAO per content table so screens depend on narrow interfaces. */
@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideCharacterDao(database: TeyvatHubDatabase): CharacterDao =
        database.characterDao()

    @Provides
    @Singleton
    fun provideWeaponDao(database: TeyvatHubDatabase): WeaponDao =
        database.weaponDao()

    @Provides
    @Singleton
    fun provideTeamDao(database: TeyvatHubDatabase): TeamDao =
        database.teamDao()

    @Provides
    @Singleton
    fun provideChecklistDao(database: TeyvatHubDatabase): ChecklistDao =
        database.checklistDao()

    @Provides
    @Singleton
    fun provideEventDao(database: TeyvatHubDatabase): GameEventDao =
        database.eventDao()
}
