package com.teyvat.hub.data.local.character

import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/** Reactive reads keep the roster list updating as favorites change. */
@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters ORDER BY name ASC")
    fun observeAll(): Flow<List<CharacterEntity>>

    /** Ids already stored, so seeding can add a bundle's new rows only. */
    @Query("SELECT id FROM characters")
    suspend fun findAllIds(): List<String>

    @Query("SELECT * FROM characters WHERE is_favorite = 1 ORDER BY name ASC")
    fun observeFavorites(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id LIMIT 1")
    suspend fun findById(id: String): CharacterEntity?

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    suspend fun search(query: String): List<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(characters: List<CharacterEntity>)

    @Query("UPDATE characters SET is_favorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: String, isFavorite: Boolean)
}
