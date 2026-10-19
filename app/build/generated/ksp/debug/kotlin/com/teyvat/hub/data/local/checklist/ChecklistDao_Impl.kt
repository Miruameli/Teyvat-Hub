package com.teyvat.hub.`data`.local.checklist

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
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
public class ChecklistDao_Impl(
  __db: RoomDatabase,
) : ChecklistDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfChecklistItemEntity: EntityInsertAdapter<ChecklistItemEntity>

  private val __insertAdapterOfChecklistItemEntity_1: EntityInsertAdapter<ChecklistItemEntity>

  private val __deleteAdapterOfChecklistItemEntity: EntityDeleteOrUpdateAdapter<ChecklistItemEntity>

  private val __updateAdapterOfChecklistItemEntity: EntityDeleteOrUpdateAdapter<ChecklistItemEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfChecklistItemEntity = object : EntityInsertAdapter<ChecklistItemEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `checklist_items` (`id`,`title`,`cadence_key`,`last_completed_at`,`is_custom`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ChecklistItemEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.cadenceKey)
        val _tmpLastCompletedAt: Long? = entity.lastCompletedAt
        if (_tmpLastCompletedAt == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmpLastCompletedAt)
        }
        val _tmp: Int = if (entity.isCustom) 1 else 0
        statement.bindLong(5, _tmp.toLong())
      }
    }
    this.__insertAdapterOfChecklistItemEntity_1 = object : EntityInsertAdapter<ChecklistItemEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `checklist_items` (`id`,`title`,`cadence_key`,`last_completed_at`,`is_custom`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ChecklistItemEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.cadenceKey)
        val _tmpLastCompletedAt: Long? = entity.lastCompletedAt
        if (_tmpLastCompletedAt == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmpLastCompletedAt)
        }
        val _tmp: Int = if (entity.isCustom) 1 else 0
        statement.bindLong(5, _tmp.toLong())
      }
    }
    this.__deleteAdapterOfChecklistItemEntity = object : EntityDeleteOrUpdateAdapter<ChecklistItemEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `checklist_items` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: ChecklistItemEntity) {
        statement.bindText(1, entity.id)
      }
    }
    this.__updateAdapterOfChecklistItemEntity = object : EntityDeleteOrUpdateAdapter<ChecklistItemEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `checklist_items` SET `id` = ?,`title` = ?,`cadence_key` = ?,`last_completed_at` = ?,`is_custom` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: ChecklistItemEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.cadenceKey)
        val _tmpLastCompletedAt: Long? = entity.lastCompletedAt
        if (_tmpLastCompletedAt == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmpLastCompletedAt)
        }
        val _tmp: Int = if (entity.isCustom) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindText(6, entity.id)
      }
    }
  }

  public override suspend fun upsertAll(items: List<ChecklistItemEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfChecklistItemEntity.insert(_connection, items)
  }

  public override suspend fun insert(item: ChecklistItemEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfChecklistItemEntity_1.insert(_connection, item)
  }

  public override suspend fun delete(item: ChecklistItemEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfChecklistItemEntity.handle(_connection, item)
  }

  public override suspend fun update(item: ChecklistItemEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfChecklistItemEntity.handle(_connection, item)
  }

  public override fun observeAll(): Flow<List<ChecklistItemEntity>> {
    val _sql: String = "SELECT * FROM checklist_items ORDER BY is_custom ASC, title ASC"
    return createFlow(__db, false, arrayOf("checklist_items")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfCadenceKey: Int = getColumnIndexOrThrow(_stmt, "cadence_key")
        val _columnIndexOfLastCompletedAt: Int = getColumnIndexOrThrow(_stmt, "last_completed_at")
        val _columnIndexOfIsCustom: Int = getColumnIndexOrThrow(_stmt, "is_custom")
        val _result: MutableList<ChecklistItemEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ChecklistItemEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpCadenceKey: String
          _tmpCadenceKey = _stmt.getText(_columnIndexOfCadenceKey)
          val _tmpLastCompletedAt: Long?
          if (_stmt.isNull(_columnIndexOfLastCompletedAt)) {
            _tmpLastCompletedAt = null
          } else {
            _tmpLastCompletedAt = _stmt.getLong(_columnIndexOfLastCompletedAt)
          }
          val _tmpIsCustom: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCustom).toInt()
          _tmpIsCustom = _tmp != 0
          _item = ChecklistItemEntity(_tmpId,_tmpTitle,_tmpCadenceKey,_tmpLastCompletedAt,_tmpIsCustom)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun count(): Int {
    val _sql: String = "SELECT COUNT(*) FROM checklist_items"
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

  public override suspend fun findById(id: String): ChecklistItemEntity? {
    val _sql: String = "SELECT * FROM checklist_items WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfCadenceKey: Int = getColumnIndexOrThrow(_stmt, "cadence_key")
        val _columnIndexOfLastCompletedAt: Int = getColumnIndexOrThrow(_stmt, "last_completed_at")
        val _columnIndexOfIsCustom: Int = getColumnIndexOrThrow(_stmt, "is_custom")
        val _result: ChecklistItemEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpCadenceKey: String
          _tmpCadenceKey = _stmt.getText(_columnIndexOfCadenceKey)
          val _tmpLastCompletedAt: Long?
          if (_stmt.isNull(_columnIndexOfLastCompletedAt)) {
            _tmpLastCompletedAt = null
          } else {
            _tmpLastCompletedAt = _stmt.getLong(_columnIndexOfLastCompletedAt)
          }
          val _tmpIsCustom: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCustom).toInt()
          _tmpIsCustom = _tmp != 0
          _result = ChecklistItemEntity(_tmpId,_tmpTitle,_tmpCadenceKey,_tmpLastCompletedAt,_tmpIsCustom)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun findByCadence(cadenceKey: String): List<ChecklistItemEntity> {
    val _sql: String = "SELECT * FROM checklist_items WHERE cadence_key = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, cadenceKey)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfCadenceKey: Int = getColumnIndexOrThrow(_stmt, "cadence_key")
        val _columnIndexOfLastCompletedAt: Int = getColumnIndexOrThrow(_stmt, "last_completed_at")
        val _columnIndexOfIsCustom: Int = getColumnIndexOrThrow(_stmt, "is_custom")
        val _result: MutableList<ChecklistItemEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ChecklistItemEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpCadenceKey: String
          _tmpCadenceKey = _stmt.getText(_columnIndexOfCadenceKey)
          val _tmpLastCompletedAt: Long?
          if (_stmt.isNull(_columnIndexOfLastCompletedAt)) {
            _tmpLastCompletedAt = null
          } else {
            _tmpLastCompletedAt = _stmt.getLong(_columnIndexOfLastCompletedAt)
          }
          val _tmpIsCustom: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCustom).toInt()
          _tmpIsCustom = _tmp != 0
          _item = ChecklistItemEntity(_tmpId,_tmpTitle,_tmpCadenceKey,_tmpLastCompletedAt,_tmpIsCustom)
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
