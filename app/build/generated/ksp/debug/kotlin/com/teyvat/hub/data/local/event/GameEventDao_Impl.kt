package com.teyvat.hub.`data`.local.event

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
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
public class GameEventDao_Impl(
  __db: RoomDatabase,
) : GameEventDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfGameEventEntity: EntityInsertAdapter<GameEventEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfGameEventEntity = object : EntityInsertAdapter<GameEventEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `events` (`id`,`title`,`kind_key`,`starts_at`,`ends_at`,`description`) VALUES (?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: GameEventEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.kindKey)
        statement.bindLong(4, entity.startsAt)
        val _tmpEndsAt: Long? = entity.endsAt
        if (_tmpEndsAt == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmpEndsAt)
        }
        statement.bindText(6, entity.description)
      }
    }
  }

  public override suspend fun upsertAll(events: List<GameEventEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfGameEventEntity.insert(_connection, events)
  }

  public override fun observeAll(): Flow<List<GameEventEntity>> {
    val _sql: String = "SELECT * FROM events ORDER BY starts_at ASC"
    return createFlow(__db, false, arrayOf("events")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfKindKey: Int = getColumnIndexOrThrow(_stmt, "kind_key")
        val _columnIndexOfStartsAt: Int = getColumnIndexOrThrow(_stmt, "starts_at")
        val _columnIndexOfEndsAt: Int = getColumnIndexOrThrow(_stmt, "ends_at")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _result: MutableList<GameEventEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: GameEventEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpKindKey: String
          _tmpKindKey = _stmt.getText(_columnIndexOfKindKey)
          val _tmpStartsAt: Long
          _tmpStartsAt = _stmt.getLong(_columnIndexOfStartsAt)
          val _tmpEndsAt: Long?
          if (_stmt.isNull(_columnIndexOfEndsAt)) {
            _tmpEndsAt = null
          } else {
            _tmpEndsAt = _stmt.getLong(_columnIndexOfEndsAt)
          }
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          _item = GameEventEntity(_tmpId,_tmpTitle,_tmpKindKey,_tmpStartsAt,_tmpEndsAt,_tmpDescription)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeByKind(kindKey: String): Flow<List<GameEventEntity>> {
    val _sql: String = "SELECT * FROM events WHERE kind_key = ? ORDER BY starts_at ASC"
    return createFlow(__db, false, arrayOf("events")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, kindKey)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfKindKey: Int = getColumnIndexOrThrow(_stmt, "kind_key")
        val _columnIndexOfStartsAt: Int = getColumnIndexOrThrow(_stmt, "starts_at")
        val _columnIndexOfEndsAt: Int = getColumnIndexOrThrow(_stmt, "ends_at")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _result: MutableList<GameEventEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: GameEventEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpKindKey: String
          _tmpKindKey = _stmt.getText(_columnIndexOfKindKey)
          val _tmpStartsAt: Long
          _tmpStartsAt = _stmt.getLong(_columnIndexOfStartsAt)
          val _tmpEndsAt: Long?
          if (_stmt.isNull(_columnIndexOfEndsAt)) {
            _tmpEndsAt = null
          } else {
            _tmpEndsAt = _stmt.getLong(_columnIndexOfEndsAt)
          }
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          _item = GameEventEntity(_tmpId,_tmpTitle,_tmpKindKey,_tmpStartsAt,_tmpEndsAt,_tmpDescription)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeUpcoming(now: Long): Flow<List<GameEventEntity>> {
    val _sql: String = "SELECT * FROM events WHERE starts_at > ? ORDER BY starts_at ASC"
    return createFlow(__db, false, arrayOf("events")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, now)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfKindKey: Int = getColumnIndexOrThrow(_stmt, "kind_key")
        val _columnIndexOfStartsAt: Int = getColumnIndexOrThrow(_stmt, "starts_at")
        val _columnIndexOfEndsAt: Int = getColumnIndexOrThrow(_stmt, "ends_at")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _result: MutableList<GameEventEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: GameEventEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpKindKey: String
          _tmpKindKey = _stmt.getText(_columnIndexOfKindKey)
          val _tmpStartsAt: Long
          _tmpStartsAt = _stmt.getLong(_columnIndexOfStartsAt)
          val _tmpEndsAt: Long?
          if (_stmt.isNull(_columnIndexOfEndsAt)) {
            _tmpEndsAt = null
          } else {
            _tmpEndsAt = _stmt.getLong(_columnIndexOfEndsAt)
          }
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          _item = GameEventEntity(_tmpId,_tmpTitle,_tmpKindKey,_tmpStartsAt,_tmpEndsAt,_tmpDescription)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun readAnyRow(): GameEventEntity? {
    val _sql: String = "SELECT * FROM events LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfKindKey: Int = getColumnIndexOrThrow(_stmt, "kind_key")
        val _columnIndexOfStartsAt: Int = getColumnIndexOrThrow(_stmt, "starts_at")
        val _columnIndexOfEndsAt: Int = getColumnIndexOrThrow(_stmt, "ends_at")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _result: GameEventEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpKindKey: String
          _tmpKindKey = _stmt.getText(_columnIndexOfKindKey)
          val _tmpStartsAt: Long
          _tmpStartsAt = _stmt.getLong(_columnIndexOfStartsAt)
          val _tmpEndsAt: Long?
          if (_stmt.isNull(_columnIndexOfEndsAt)) {
            _tmpEndsAt = null
          } else {
            _tmpEndsAt = _stmt.getLong(_columnIndexOfEndsAt)
          }
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          _result = GameEventEntity(_tmpId,_tmpTitle,_tmpKindKey,_tmpStartsAt,_tmpEndsAt,_tmpDescription)
        } else {
          _result = null
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
