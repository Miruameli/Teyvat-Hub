package com.teyvat.hub.data.local.checklist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Persisted checklist task.
 *
 * [lastCompletedAt] holds epoch millis of the last completion, or null. Reset
 * decisions are made in the domain layer using this timestamp.
 */
@Entity(tableName = "checklist_items")
data class ChecklistItemEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "cadence_key")
    val cadenceKey: String,

    @ColumnInfo(name = "last_completed_at")
    val lastCompletedAt: Long? = null,

    @ColumnInfo(name = "is_custom", defaultValue = "0")
    val isCustom: Boolean = false,
)
