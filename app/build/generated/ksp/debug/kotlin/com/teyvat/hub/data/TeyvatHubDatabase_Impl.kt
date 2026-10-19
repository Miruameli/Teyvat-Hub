package com.teyvat.hub.`data`

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.teyvat.hub.`data`.local.character.CharacterDao
import com.teyvat.hub.`data`.local.character.CharacterDao_Impl
import com.teyvat.hub.`data`.local.checklist.ChecklistDao
import com.teyvat.hub.`data`.local.checklist.ChecklistDao_Impl
import com.teyvat.hub.`data`.local.event.GameEventDao
import com.teyvat.hub.`data`.local.event.GameEventDao_Impl
import com.teyvat.hub.`data`.local.team.TeamDao
import com.teyvat.hub.`data`.local.team.TeamDao_Impl
import com.teyvat.hub.`data`.local.weapon.WeaponDao
import com.teyvat.hub.`data`.local.weapon.WeaponDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TeyvatHubDatabase_Impl : TeyvatHubDatabase() {
  private val _characterDao: Lazy<CharacterDao> = lazy {
    CharacterDao_Impl(this)
  }

  private val _weaponDao: Lazy<WeaponDao> = lazy {
    WeaponDao_Impl(this)
  }

  private val _teamDao: Lazy<TeamDao> = lazy {
    TeamDao_Impl(this)
  }

  private val _checklistDao: Lazy<ChecklistDao> = lazy {
    ChecklistDao_Impl(this)
  }

  private val _gameEventDao: Lazy<GameEventDao> = lazy {
    GameEventDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "c14122f3149e78b2fd3eaa5e420be27c", "8f24c3f86baad8d82035c7cc1baf1d3b") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `characters` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `element_key` TEXT NOT NULL, `weapon_type_key` TEXT NOT NULL, `rarity_stars` INTEGER NOT NULL, `region` TEXT NOT NULL, `role_key` TEXT NOT NULL, `is_favorite` INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `weapons` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `type_key` TEXT NOT NULL, `rarity_stars` INTEGER NOT NULL, `base_attack` INTEGER NOT NULL, `sub_stat` TEXT NOT NULL, `sub_stat_value` TEXT NOT NULL, `passive` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `teams` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `member_ids` TEXT NOT NULL, `synergy_notes` TEXT NOT NULL, `is_template` INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `checklist_items` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `cadence_key` TEXT NOT NULL, `last_completed_at` INTEGER, `is_custom` INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `events` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `kind_key` TEXT NOT NULL, `starts_at` INTEGER NOT NULL, `ends_at` INTEGER, `description` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c14122f3149e78b2fd3eaa5e420be27c')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `characters`")
        connection.execSQL("DROP TABLE IF EXISTS `weapons`")
        connection.execSQL("DROP TABLE IF EXISTS `teams`")
        connection.execSQL("DROP TABLE IF EXISTS `checklist_items`")
        connection.execSQL("DROP TABLE IF EXISTS `events`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsCharacters: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCharacters.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCharacters.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCharacters.put("element_key", TableInfo.Column("element_key", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCharacters.put("weapon_type_key", TableInfo.Column("weapon_type_key", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCharacters.put("rarity_stars", TableInfo.Column("rarity_stars", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCharacters.put("region", TableInfo.Column("region", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCharacters.put("role_key", TableInfo.Column("role_key", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCharacters.put("is_favorite", TableInfo.Column("is_favorite", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCharacters: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCharacters: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoCharacters: TableInfo = TableInfo("characters", _columnsCharacters, _foreignKeysCharacters, _indicesCharacters)
        val _existingCharacters: TableInfo = read(connection, "characters")
        if (!_infoCharacters.equals(_existingCharacters)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |characters(com.teyvat.hub.data.local.character.CharacterEntity).
              | Expected:
              |""".trimMargin() + _infoCharacters + """
              |
              | Found:
              |""".trimMargin() + _existingCharacters)
        }
        val _columnsWeapons: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsWeapons.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeapons.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeapons.put("type_key", TableInfo.Column("type_key", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeapons.put("rarity_stars", TableInfo.Column("rarity_stars", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeapons.put("base_attack", TableInfo.Column("base_attack", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeapons.put("sub_stat", TableInfo.Column("sub_stat", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeapons.put("sub_stat_value", TableInfo.Column("sub_stat_value", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeapons.put("passive", TableInfo.Column("passive", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysWeapons: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesWeapons: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoWeapons: TableInfo = TableInfo("weapons", _columnsWeapons, _foreignKeysWeapons, _indicesWeapons)
        val _existingWeapons: TableInfo = read(connection, "weapons")
        if (!_infoWeapons.equals(_existingWeapons)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |weapons(com.teyvat.hub.data.local.weapon.WeaponEntity).
              | Expected:
              |""".trimMargin() + _infoWeapons + """
              |
              | Found:
              |""".trimMargin() + _existingWeapons)
        }
        val _columnsTeams: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTeams.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeams.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeams.put("member_ids", TableInfo.Column("member_ids", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeams.put("synergy_notes", TableInfo.Column("synergy_notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeams.put("is_template", TableInfo.Column("is_template", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTeams: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTeams: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTeams: TableInfo = TableInfo("teams", _columnsTeams, _foreignKeysTeams, _indicesTeams)
        val _existingTeams: TableInfo = read(connection, "teams")
        if (!_infoTeams.equals(_existingTeams)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |teams(com.teyvat.hub.data.local.team.TeamEntity).
              | Expected:
              |""".trimMargin() + _infoTeams + """
              |
              | Found:
              |""".trimMargin() + _existingTeams)
        }
        val _columnsChecklistItems: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsChecklistItems.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsChecklistItems.put("title", TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsChecklistItems.put("cadence_key", TableInfo.Column("cadence_key", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsChecklistItems.put("last_completed_at", TableInfo.Column("last_completed_at", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsChecklistItems.put("is_custom", TableInfo.Column("is_custom", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysChecklistItems: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesChecklistItems: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoChecklistItems: TableInfo = TableInfo("checklist_items", _columnsChecklistItems, _foreignKeysChecklistItems, _indicesChecklistItems)
        val _existingChecklistItems: TableInfo = read(connection, "checklist_items")
        if (!_infoChecklistItems.equals(_existingChecklistItems)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |checklist_items(com.teyvat.hub.data.local.checklist.ChecklistItemEntity).
              | Expected:
              |""".trimMargin() + _infoChecklistItems + """
              |
              | Found:
              |""".trimMargin() + _existingChecklistItems)
        }
        val _columnsEvents: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsEvents.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("title", TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("kind_key", TableInfo.Column("kind_key", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("starts_at", TableInfo.Column("starts_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("ends_at", TableInfo.Column("ends_at", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("description", TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysEvents: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesEvents: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoEvents: TableInfo = TableInfo("events", _columnsEvents, _foreignKeysEvents, _indicesEvents)
        val _existingEvents: TableInfo = read(connection, "events")
        if (!_infoEvents.equals(_existingEvents)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |events(com.teyvat.hub.data.local.event.GameEventEntity).
              | Expected:
              |""".trimMargin() + _infoEvents + """
              |
              | Found:
              |""".trimMargin() + _existingEvents)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "characters", "weapons", "teams", "checklist_items", "events")
  }

  public override fun clearAllTables() {
    super.performClear(false, "characters", "weapons", "teams", "checklist_items", "events")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(CharacterDao::class, CharacterDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(WeaponDao::class, WeaponDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(TeamDao::class, TeamDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ChecklistDao::class, ChecklistDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(GameEventDao::class, GameEventDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun characterDao(): CharacterDao = _characterDao.value

  public override fun weaponDao(): WeaponDao = _weaponDao.value

  public override fun teamDao(): TeamDao = _teamDao.value

  public override fun checklistDao(): ChecklistDao = _checklistDao.value

  public override fun eventDao(): GameEventDao = _gameEventDao.value
}
