package com.teyvat.hub.data.local.checklist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {

    @Query("SELECT * FROM checklist_items ORDER BY is_custom ASC, title ASC")
    fun observeAll(): Flow<List<ChecklistItemEntity>>

    @Query("SELECT COUNT(*) FROM checklist_items")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ChecklistItemEntity>)

    @Insert
    suspend fun insert(item: ChecklistItemEntity)

    @Update
    suspend fun update(item: ChecklistItemEntity)

    @Delete
    suspend fun delete(item: ChecklistItemEntity)

    @Query("SELECT * FROM checklist_items WHERE id = :id LIMIT 1")
    suspend fun findById(id: String): ChecklistItemEntity?

    @Query("SELECT * FROM checklist_items WHERE cadence_key = :cadenceKey")
    suspend fun findByCadence(cadenceKey: String): List<ChecklistItemEntity>
}
