package com.teyvat.hub.data.local.weapon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** Persisted weapon row. */
@Entity(tableName = "weapons")
data class WeaponEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "type_key")
    val typeKey: String,

    @ColumnInfo(name = "rarity_stars")
    val rarityStars: Int,

    @ColumnInfo(name = "base_attack")
    val baseAttack: Int,

    @ColumnInfo(name = "sub_stat")
    val subStat: String,

    @ColumnInfo(name = "sub_stat_value")
    val subStatValue: String,

    @ColumnInfo(name = "passive")
    val passive: String,
)
