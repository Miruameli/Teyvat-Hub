package com.teyvat.hub.application.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.teyvat.hub.data.local.weapon.WeaponDao
import com.teyvat.hub.data.mapper.toDomain
import com.teyvat.hub.domain.character.WeaponType
import com.teyvat.hub.domain.equipment.Weapon

/**
 * Single source of truth for the weapon list.
 *
 * Reads are reactive so a seeding pass that adds bundle rows reaches the
 * screen without a restart. The player chooses a family, and a type filter is
 * the only interesting question a weapon list has, so the filter lives here
 * rather than in the view model.
 */
interface WeaponRepository {
    fun observeAll(): Flow<List<Weapon>>
    fun observeByType(type: WeaponType): Flow<List<Weapon>>
}

@Singleton
class LocalWeaponRepository @Inject constructor(
    private val dao: WeaponDao,
) : WeaponRepository {

    override fun observeAll(): Flow<List<Weapon>> =
        dao.observeAll().map { rows -> rows.map { it.toDomain() } }

    override fun observeByType(type: WeaponType): Flow<List<Weapon>> =
        dao.observeByType(type.key).map { rows -> rows.map { it.toDomain() } }
}