package com.teyvat.hub.data.local.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Persisted form of a character.
 *
 * Enum values are stored as their stable string [com.teyvat.hub.domain.model]
 * keys, not ordinals, so that reordering the enum cannot corrupt saved rows.
 */
@Entity(tableName = "characters")
data class CharacterEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "element_key")
    val elementKey: String,

    @ColumnInfo(name = "weapon_type_key")
    val weaponTypeKey: String,

    @ColumnInfo(name = "rarity_stars")
    val rarityStars: Int,

    @ColumnInfo(name = "region")
    val region: String,

    @ColumnInfo(name = "role_key")
    val roleKey: String,

    @ColumnInfo(name = "is_favorite", defaultValue = "0")
    val isFavorite: Boolean = false,
)
