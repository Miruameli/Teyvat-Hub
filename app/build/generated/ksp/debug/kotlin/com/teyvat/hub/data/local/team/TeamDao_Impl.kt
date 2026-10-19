package com.teyvat.hub.`data`.local.team

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
public class TeamDao_Impl(
  __db: RoomDatabase,
) : TeamDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTeamEntity: EntityInsertAdapter<TeamEntity>

  private val __insertAdapterOfTeamEntity_1: EntityInsertAdapter<TeamEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTeamEntity = object : EntityInsertAdapter<TeamEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `teams` (`id`,`name`,`member_ids`,`synergy_notes`,`is_template`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TeamEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.memberIds)
        statement.bindText(4, entity.synergyNotes)
        val _tmp: Int = if (entity.isTemplate) 1 else 0
        statement.bindLong(5, _tmp.toLong())
      }
    }
    this.__insertAdapterOfTeamEntity_1 = object : EntityInsertAdapter<TeamEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `teams` (`id`,`name`,`member_ids`,`synergy_notes`,`is_template`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TeamEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.memberIds)
        statement.bindText(4, entity.synergyNotes)
        val _tmp: Int = if (entity.isTemplate) 1 else 0
        statement.bindLong(5, _tmp.toLong())
      }
    }
  }

  public override suspend fun upsertAll(teams: List<TeamEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTeamEntity.insert(_connection, teams)
  }

  public override suspend fun insert(team: TeamEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTeamEntity_1.insert(_connection, team)
  }

  public override fun observeTemplates(): Flow<List<TeamEntity>> {
    val _sql: String = "SELECT * FROM teams WHERE is_template = 1 ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("teams")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfMemberIds: Int = getColumnIndexOrThrow(_stmt, "member_ids")
        val _columnIndexOfSynergyNotes: Int = getColumnIndexOrThrow(_stmt, "synergy_notes")
        val _columnIndexOfIsTemplate: Int = getColumnIndexOrThrow(_stmt, "is_template")
        val _result: MutableList<TeamEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TeamEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpMemberIds: String
          _tmpMemberIds = _stmt.getText(_columnIndexOfMemberIds)
          val _tmpSynergyNotes: String
          _tmpSynergyNotes = _stmt.getText(_columnIndexOfSynergyNotes)
          val _tmpIsTemplate: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsTemplate).toInt()
          _tmpIsTemplate = _tmp != 0
          _item = TeamEntity(_tmpId,_tmpName,_tmpMemberIds,_tmpSynergyNotes,_tmpIsTemplate)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeSaved(): Flow<List<TeamEntity>> {
    val _sql: String = "SELECT * FROM teams WHERE is_template = 0 ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("teams")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfMemberIds: Int = getColumnIndexOrThrow(_stmt, "member_ids")
        val _columnIndexOfSynergyNotes: Int = getColumnIndexOrThrow(_stmt, "synergy_notes")
        val _columnIndexOfIsTemplate: Int = getColumnIndexOrThrow(_stmt, "is_template")
        val _result: MutableList<TeamEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TeamEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpMemberIds: String
          _tmpMemberIds = _stmt.getText(_columnIndexOfMemberIds)
          val _tmpSynergyNotes: String
          _tmpSynergyNotes = _stmt.getText(_columnIndexOfSynergyNotes)
          val _tmpIsTemplate: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsTemplate).toInt()
          _tmpIsTemplate = _tmp != 0
          _item = TeamEntity(_tmpId,_tmpName,_tmpMemberIds,_tmpSynergyNotes,_tmpIsTemplate)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun findById(id: String): TeamEntity? {
    val _sql: String = "SELECT * FROM teams WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfMemberIds: Int = getColumnIndexOrThrow(_stmt, "member_ids")
        val _columnIndexOfSynergyNotes: Int = getColumnIndexOrThrow(_stmt, "synergy_notes")
        val _columnIndexOfIsTemplate: Int = getColumnIndexOrThrow(_stmt, "is_template")
        val _result: TeamEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpMemberIds: String
          _tmpMemberIds = _stmt.getText(_columnIndexOfMemberIds)
          val _tmpSynergyNotes: String
          _tmpSynergyNotes = _stmt.getText(_columnIndexOfSynergyNotes)
          val _tmpIsTemplate: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsTemplate).toInt()
          _tmpIsTemplate = _tmp != 0
          _result = TeamEntity(_tmpId,_tmpName,_tmpMemberIds,_tmpSynergyNotes,_tmpIsTemplate)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteById(id: String) {
    val _sql: String = "DELETE FROM teams WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
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
