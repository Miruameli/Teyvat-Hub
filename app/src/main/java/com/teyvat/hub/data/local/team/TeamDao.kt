package com.teyvat.hub.data.local.team

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * A saved team.
 *
 * Member ids are stored as a delimiter-joined string because teams are always
 * read whole and are capped at four members by the domain layer; a join table
 * would add a migration path for no query we need.
 */
@Entity(tableName = "teams")
data class TeamEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "member_ids")
    val memberIds: String,

    @ColumnInfo(name = "synergy_notes")
    val synergyNotes: String,

    @ColumnInfo(name = "is_template", defaultValue = "0")
    val isTemplate: Boolean,
)

@Dao
interface TeamDao {

    @Query("SELECT * FROM teams WHERE is_template = 1 ORDER BY name ASC")
    fun observeTemplates(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams WHERE is_template = 0 ORDER BY name ASC")
    fun observeSaved(): Flow<List<TeamEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(teams: List<TeamEntity>)

    @Insert
    suspend fun insert(team: TeamEntity)

    @Query("DELETE FROM teams WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM teams WHERE id = :id LIMIT 1")
    suspend fun findById(id: String): TeamEntity?
}
