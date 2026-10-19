package com.teyvat.hub.data.local.event

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** Persisted event or banner window. */
@Entity(tableName = "events")
data class GameEventEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "kind_key")
    val kindKey: String,

    @ColumnInfo(name = "starts_at")
    val startsAt: Long,

    @ColumnInfo(name = "ends_at")
    val endsAt: Long?,

    @ColumnInfo(name = "description")
    val description: String,
)

@Dao
interface GameEventDao {

    /** Ordered by start so the calendar reads chronologically. */
    @Query("SELECT * FROM events ORDER BY starts_at ASC")
    fun observeAll(): Flow<List<GameEventEntity>>

    @Query("SELECT * FROM events WHERE kind_key = :kindKey ORDER BY starts_at ASC")
    fun observeByKind(kindKey: String): Flow<List<GameEventEntity>>

    @Query("SELECT * FROM events WHERE starts_at > :now ORDER BY starts_at ASC")
    fun observeUpcoming(now: Long): Flow<List<GameEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(events: List<GameEventEntity>)

    /** One-shot read used by seeding, which only needs the current contents. */
    @Query("SELECT * FROM events LIMIT 1")
    suspend fun readAnyRow(): GameEventEntity?
}
