package com.teyvat.hub.application

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import com.teyvat.hub.data.local.checklist.ChecklistDao
import com.teyvat.hub.data.local.checklist.ChecklistItemEntity
import com.teyvat.hub.domain.checklist.ResetCadence
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Verifies the repository contract against an in-memory DAO fake.
 *
 * Room itself is covered by instrumentation tests; here we only prove the
 * repository maps rows correctly and records completion timestamps.
 */
class ChecklistRepositoryTest {

    @Test
    fun `observeAll maps stored rows to domain items`() = runTest {
        val dao = FakeChecklistDao()
        dao.upsertAll(
            listOf(
                ChecklistItemEntity("a", "Komisi", "daily", null, false),
                ChecklistItemEntity("b", "Boss", "weekly", null, false),
            ),
        )
        val repository = LocalChecklistRepository(dao)

        val items = repository.observeAll().firstValue()

        assertEquals(2, items.size)
        assertEquals(ResetCadence.DAILY, items.first { it.id == "a" }.cadence)
        assertEquals(ResetCadence.WEEKLY, items.first { it.id == "b" }.cadence)
    }

    @Test
    fun `markCompleted stores the completion timestamp`() = runTest {
        val dao = FakeChecklistDao()
        dao.upsertAll(listOf(ChecklistItemEntity("a", "Komisi", "daily", null, false)))
        val repository = LocalChecklistRepository(dao)

        repository.markCompleted("a", at = 1_000L)

        val stored = dao.findById("a")
        assertEquals(1_000L, stored?.lastCompletedAt)
    }

    @Test
    fun `markCompleted is ignored for unknown id`() = runTest {
        val dao = FakeChecklistDao()
        val repository = LocalChecklistRepository(dao)

        repository.markCompleted("missing", at = 1_000L)

        assertTrue(dao.rows.value.isEmpty())
    }

    @Test
    fun `custom task is persisted as custom with no reset`() = runTest {
        val dao = FakeChecklistDao()
        val repository = LocalChecklistRepository(dao)

        repository.addCustomTask("Kumpulkan mora")

        val stored = dao.rows.value.single()
        assertTrue(stored.isCustom)
        assertEquals("none", stored.cadenceKey)
        assertEquals("Kumpulkan mora", stored.title)
    }

    @Test
    fun `custom task with blank title is not stored`() = runTest {
        val dao = FakeChecklistDao()
        val repository = LocalChecklistRepository(dao)

        repository.addCustomTask("   ")

        assertTrue(dao.rows.value.isEmpty())
    }

    @Test
    fun `delete removes the stored row`() = runTest {
        val dao = FakeChecklistDao()
        dao.upsertAll(listOf(ChecklistItemEntity("a", "Komisi", "daily", null, false)))
        val repository = LocalChecklistRepository(dao)

        repository.delete("a")

        assertTrue(dao.rows.value.isEmpty())
    }

    @Test
    fun `upsert replaces a row with the same id instead of duplicating`() = runTest {
        val dao = FakeChecklistDao()
        dao.upsertAll(listOf(ChecklistItemEntity("a", "Lama", "daily", null, false)))

        dao.upsertAll(listOf(ChecklistItemEntity("a", "Baru", "daily", null, false)))

        assertEquals(1, dao.rows.value.size)
        assertEquals("Baru", dao.rows.value.single().title)
    }

    /** Minimal in-memory DAO stand-in covering only repository behaviour. */
    private class FakeChecklistDao : ChecklistDao {

        val rows = MutableStateFlow<List<ChecklistItemEntity>>(emptyList())

        override fun observeAll(): Flow<List<ChecklistItemEntity>> = rows

        override suspend fun count(): Int = rows.value.size

        override suspend fun upsertAll(items: List<ChecklistItemEntity>) {
            val merged = (rows.value + items).associateBy { it.id }
            rows.value = merged.values.toList()
        }

        override suspend fun insert(item: ChecklistItemEntity) {
            rows.value = rows.value + item
        }

        override suspend fun update(item: ChecklistItemEntity) {
            rows.value = rows.value.map { if (it.id == item.id) item else it }
        }

        override suspend fun delete(item: ChecklistItemEntity) {
            rows.value = rows.value.filterNot { it.id == item.id }
        }

        override suspend fun findById(id: String): ChecklistItemEntity? =
            rows.value.firstOrNull { it.id == id }

        override suspend fun findByCadence(cadenceKey: String): List<ChecklistItemEntity> =
            rows.value.filter { it.cadenceKey == cadenceKey }
    }

    private suspend fun <T> Flow<T>.firstValue(): T {
        var result: T? = null
        take(1).collect { result = it }
        @Suppress("UNCHECKED_CAST")
        return result as T
    }
}
