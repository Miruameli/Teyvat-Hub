package com.teyvat.hub.`data`.local.character

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CharacterDao_Impl(
  __db: RoomDatabase,
) : CharacterDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCharacterEntity: EntityInsertAdapter<CharacterEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfCharacterEntity = object : EntityInsertAdapter<CharacterEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `characters` (`id`,`name`,`element_key`,`weapon_type_key`,`rarity_stars`,`region`,`role_key`,`is_favorite`) VALUES (?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CharacterEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.elementKey)
        statement.bindText(4, entity.weaponTypeKey)
        statement.bindLong(5, entity.rarityStars.toLong())
        statement.bindText(6, entity.region)
        statement.bindText(7, entity.roleKey)
        val _tmp: Int = if (entity.isFavorite) 1 else 0
        statement.bindLong(8, _tmp.toLong())
      }
    }
  }

  public override suspend fun upsertAll(characters: List<CharacterEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfCharacterEntity.insert(_connection, characters)
  }

  public override fun observeAll(): Flow<List<CharacterEntity>> {
    val _sql: String = "SELECT * FROM characters ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("characters")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfElementKey: Int = getColumnIndexOrThrow(_stmt, "element_key")
        val _columnIndexOfWeaponTypeKey: Int = getColumnIndexOrThrow(_stmt, "weapon_type_key")
        val _columnIndexOfRarityStars: Int = getColumnIndexOrThrow(_stmt, "rarity_stars")
        val _columnIndexOfRegion: Int = getColumnIndexOrThrow(_stmt, "region")
        val _columnIndexOfRoleKey: Int = getColumnIndexOrThrow(_stmt, "role_key")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "is_favorite")
        val _result: MutableList<CharacterEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CharacterEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpElementKey: String
          _tmpElementKey = _stmt.getText(_columnIndexOfElementKey)
          val _tmpWeaponTypeKey: String
          _tmpWeaponTypeKey = _stmt.getText(_columnIndexOfWeaponTypeKey)
          val _tmpRarityStars: Int
          _tmpRarityStars = _stmt.getLong(_columnIndexOfRarityStars).toInt()
          val _tmpRegion: String
          _tmpRegion = _stmt.getText(_columnIndexOfRegion)
          val _tmpRoleKey: String
          _tmpRoleKey = _stmt.getText(_columnIndexOfRoleKey)
          val _tmpIsFavorite: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp != 0
          _item = CharacterEntity(_tmpId,_tmpName,_tmpElementKey,_tmpWeaponTypeKey,_tmpRarityStars,_tmpRegion,_tmpRoleKey,_tmpIsFavorite)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeFavorites(): Flow<List<CharacterEntity>> {
    val _sql: String = "SELECT * FROM characters WHERE is_favorite = 1 ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("characters")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfElementKey: Int = getColumnIndexOrThrow(_stmt, "element_key")
        val _columnIndexOfWeaponTypeKey: Int = getColumnIndexOrThrow(_stmt, "weapon_type_key")
        val _columnIndexOfRarityStars: Int = getColumnIndexOrThrow(_stmt, "rarity_stars")
        val _columnIndexOfRegion: Int = getColumnIndexOrThrow(_stmt, "region")
        val _columnIndexOfRoleKey: Int = getColumnIndexOrThrow(_stmt, "role_key")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "is_favorite")
        val _result: MutableList<CharacterEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CharacterEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpElementKey: String
          _tmpElementKey = _stmt.getText(_columnIndexOfElementKey)
          val _tmpWeaponTypeKey: String
          _tmpWeaponTypeKey = _stmt.getText(_columnIndexOfWeaponTypeKey)
          val _tmpRarityStars: Int
          _tmpRarityStars = _stmt.getLong(_columnIndexOfRarityStars).toInt()
          val _tmpRegion: String
          _tmpRegion = _stmt.getText(_columnIndexOfRegion)
          val _tmpRoleKey: String
          _tmpRoleKey = _stmt.getText(_columnIndexOfRoleKey)
          val _tmpIsFavorite: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp != 0
          _item = CharacterEntity(_tmpId,_tmpName,_tmpElementKey,_tmpWeaponTypeKey,_tmpRarityStars,_tmpRegion,_tmpRoleKey,_tmpIsFavorite)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun findById(id: String): CharacterEntity? {
    val _sql: String = "SELECT * FROM characters WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfElementKey: Int = getColumnIndexOrThrow(_stmt, "element_key")
        val _columnIndexOfWeaponTypeKey: Int = getColumnIndexOrThrow(_stmt, "weapon_type_key")
        val _columnIndexOfRarityStars: Int = getColumnIndexOrThrow(_stmt, "rarity_stars")
        val _columnIndexOfRegion: Int = getColumnIndexOrThrow(_stmt, "region")
        val _columnIndexOfRoleKey: Int = getColumnIndexOrThrow(_stmt, "role_key")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "is_favorite")
        val _result: CharacterEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpElementKey: String
          _tmpElementKey = _stmt.getText(_columnIndexOfElementKey)
          val _tmpWeaponTypeKey: String
          _tmpWeaponTypeKey = _stmt.getText(_columnIndexOfWeaponTypeKey)
          val _tmpRarityStars: Int
          _tmpRarityStars = _stmt.getLong(_columnIndexOfRarityStars).toInt()
          val _tmpRegion: String
          _tmpRegion = _stmt.getText(_columnIndexOfRegion)
          val _tmpRoleKey: String
          _tmpRoleKey = _stmt.getText(_columnIndexOfRoleKey)
          val _tmpIsFavorite: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp != 0
          _result = CharacterEntity(_tmpId,_tmpName,_tmpElementKey,_tmpWeaponTypeKey,_tmpRarityStars,_tmpRegion,_tmpRoleKey,_tmpIsFavorite)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun search(query: String): List<CharacterEntity> {
    val _sql: String = "SELECT * FROM characters WHERE name LIKE '%' || ? || '%' ORDER BY name ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, query)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfElementKey: Int = getColumnIndexOrThrow(_stmt, "element_key")
        val _columnIndexOfWeaponTypeKey: Int = getColumnIndexOrThrow(_stmt, "weapon_type_key")
        val _columnIndexOfRarityStars: Int = getColumnIndexOrThrow(_stmt, "rarity_stars")
        val _columnIndexOfRegion: Int = getColumnIndexOrThrow(_stmt, "region")
        val _columnIndexOfRoleKey: Int = getColumnIndexOrThrow(_stmt, "role_key")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "is_favorite")
        val _result: MutableList<CharacterEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CharacterEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpElementKey: String
          _tmpElementKey = _stmt.getText(_columnIndexOfElementKey)
          val _tmpWeaponTypeKey: String
          _tmpWeaponTypeKey = _stmt.getText(_columnIndexOfWeaponTypeKey)
          val _tmpRarityStars: Int
          _tmpRarityStars = _stmt.getLong(_columnIndexOfRarityStars).toInt()
          val _tmpRegion: String
          _tmpRegion = _stmt.getText(_columnIndexOfRegion)
          val _tmpRoleKey: String
          _tmpRoleKey = _stmt.getText(_columnIndexOfRoleKey)
          val _tmpIsFavorite: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp != 0
          _item = CharacterEntity(_tmpId,_tmpName,_tmpElementKey,_tmpWeaponTypeKey,_tmpRarityStars,_tmpRegion,_tmpRoleKey,_tmpIsFavorite)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countRows(): Int {
    val _sql: String = "SELECT COUNT(*) FROM characters"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun setFavorite(id: String, isFavorite: Boolean) {
    val _sql: String = "UPDATE characters SET is_favorite = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: Int = if (isFavorite) 1 else 0
        _stmt.bindLong(_argIndex, _tmp.toLong())
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
