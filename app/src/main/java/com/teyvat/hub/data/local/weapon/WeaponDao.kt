package com.teyvat.hub.data.local.weapon

import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeaponDao {

    @Query("SELECT * FROM weapons ORDER BY rarity_stars DESC, name ASC")
    fun observeAll(): Flow<List<WeaponEntity>>

    /** Ids already stored, so seeding can add a bundle's new rows only. */
    @Query("SELECT id FROM weapons")
    suspend fun findAllIds(): List<String>

    @Query("SELECT * FROM weapons WHERE type_key = :typeKey ORDER BY name ASC")
    fun observeByType(typeKey: String): Flow<List<WeaponEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(weapons: List<WeaponEntity>)
}
