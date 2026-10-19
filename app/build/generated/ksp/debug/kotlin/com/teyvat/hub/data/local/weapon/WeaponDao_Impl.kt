package com.teyvat.hub.`data`.local.weapon

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
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
public class WeaponDao_Impl(
  __db: RoomDatabase,
) : WeaponDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfWeaponEntity: EntityInsertAdapter<WeaponEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfWeaponEntity = object : EntityInsertAdapter<WeaponEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `weapons` (`id`,`name`,`type_key`,`rarity_stars`,`base_attack`,`sub_stat`,`sub_stat_value`,`passive`) VALUES (?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: WeaponEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.typeKey)
        statement.bindLong(4, entity.rarityStars.toLong())
        statement.bindLong(5, entity.baseAttack.toLong())
        statement.bindText(6, entity.subStat)
        statement.bindText(7, entity.subStatValue)
        statement.bindText(8, entity.passive)
      }
    }
  }

  public override suspend fun upsertAll(weapons: List<WeaponEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfWeaponEntity.insert(_connection, weapons)
  }

  public override fun observeAll(): Flow<List<WeaponEntity>> {
    val _sql: String = "SELECT * FROM weapons ORDER BY rarity_stars DESC, name ASC"
    return createFlow(__db, false, arrayOf("weapons")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfTypeKey: Int = getColumnIndexOrThrow(_stmt, "type_key")
        val _columnIndexOfRarityStars: Int = getColumnIndexOrThrow(_stmt, "rarity_stars")
        val _columnIndexOfBaseAttack: Int = getColumnIndexOrThrow(_stmt, "base_attack")
        val _columnIndexOfSubStat: Int = getColumnIndexOrThrow(_stmt, "sub_stat")
        val _columnIndexOfSubStatValue: Int = getColumnIndexOrThrow(_stmt, "sub_stat_value")
        val _columnIndexOfPassive: Int = getColumnIndexOrThrow(_stmt, "passive")
        val _result: MutableList<WeaponEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: WeaponEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpTypeKey: String
          _tmpTypeKey = _stmt.getText(_columnIndexOfTypeKey)
          val _tmpRarityStars: Int
          _tmpRarityStars = _stmt.getLong(_columnIndexOfRarityStars).toInt()
          val _tmpBaseAttack: Int
          _tmpBaseAttack = _stmt.getLong(_columnIndexOfBaseAttack).toInt()
          val _tmpSubStat: String
          _tmpSubStat = _stmt.getText(_columnIndexOfSubStat)
          val _tmpSubStatValue: String
          _tmpSubStatValue = _stmt.getText(_columnIndexOfSubStatValue)
          val _tmpPassive: String
          _tmpPassive = _stmt.getText(_columnIndexOfPassive)
          _item = WeaponEntity(_tmpId,_tmpName,_tmpTypeKey,_tmpRarityStars,_tmpBaseAttack,_tmpSubStat,_tmpSubStatValue,_tmpPassive)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeByType(typeKey: String): Flow<List<WeaponEntity>> {
    val _sql: String = "SELECT * FROM weapons WHERE type_key = ? ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("weapons")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, typeKey)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfTypeKey: Int = getColumnIndexOrThrow(_stmt, "type_key")
        val _columnIndexOfRarityStars: Int = getColumnIndexOrThrow(_stmt, "rarity_stars")
        val _columnIndexOfBaseAttack: Int = getColumnIndexOrThrow(_stmt, "base_attack")
        val _columnIndexOfSubStat: Int = getColumnIndexOrThrow(_stmt, "sub_stat")
        val _columnIndexOfSubStatValue: Int = getColumnIndexOrThrow(_stmt, "sub_stat_value")
        val _columnIndexOfPassive: Int = getColumnIndexOrThrow(_stmt, "passive")
        val _result: MutableList<WeaponEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: WeaponEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpTypeKey: String
          _tmpTypeKey = _stmt.getText(_columnIndexOfTypeKey)
          val _tmpRarityStars: Int
          _tmpRarityStars = _stmt.getLong(_columnIndexOfRarityStars).toInt()
          val _tmpBaseAttack: Int
          _tmpBaseAttack = _stmt.getLong(_columnIndexOfBaseAttack).toInt()
          val _tmpSubStat: String
          _tmpSubStat = _stmt.getText(_columnIndexOfSubStat)
          val _tmpSubStatValue: String
          _tmpSubStatValue = _stmt.getText(_columnIndexOfSubStatValue)
          val _tmpPassive: String
          _tmpPassive = _stmt.getText(_columnIndexOfPassive)
          _item = WeaponEntity(_tmpId,_tmpName,_tmpTypeKey,_tmpRarityStars,_tmpBaseAttack,_tmpSubStat,_tmpSubStatValue,_tmpPassive)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
