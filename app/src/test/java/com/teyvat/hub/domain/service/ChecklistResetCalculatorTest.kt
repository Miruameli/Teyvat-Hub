package com.teyvat.hub.domain.service

import com.teyvat.hub.domain.model.ChecklistItem
import com.teyvat.hub.domain.model.ResetCadence
import java.util.Calendar
import java.util.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Exercises the reset rules that drive whether a task shows as pending. */
class ChecklistResetCalculatorTest {

    private val serverZone: TimeZone = TimeZone.getTimeZone("Asia/Shanghai")
    private val calculator = ChecklistResetCalculator(serverZone)

    private fun serverTime(
        year: Int,
        month: Int, // Calendar is zero-based
        day: Int,
        hour: Int,
        minute: Int = 0,
    ): Long = Calendar.getInstance(serverZone).apply {
        clear()
        set(year, month, day, hour, minute, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    @Test
    fun `daily window starts at 04_00 server time`() {
        val now = serverTime(2026, Calendar.OCTOBER, 18, 12, 0)
        val item = ChecklistItem("id", "Daily boss", ResetCadence.DAILY)

        val start = calculator.windowStart(item, now)

        assertEquals(serverTime(2026, Calendar.OCTOBER, 18, 4), start)
    }

    @Test
    fun `daily window rolls back before 04_00 server time`() {
        // 02:00 on the 18th still belongs to the window that opened on the 17th.
        val now = serverTime(2026, Calendar.OCTOBER, 18, 2, 0)
        val item = ChecklistItem("id", "Daily boss", ResetCadence.DAILY)

        val start = calculator.windowStart(item, now)

        assertEquals(serverTime(2026, Calendar.OCTOBER, 17, 4), start)
    }

    @Test
    fun `weekly window starts on Monday`() {
        // 2026-10-18 is a Sunday, so the week began on Monday the 12th.
        val now = serverTime(2026, Calendar.OCTOBER, 18, 9, 0)
        val item = ChecklistItem("id", "Weekly boss", ResetCadence.WEEKLY)

        val start = calculator.windowStart(item, now)

        assertEquals(serverTime(2026, Calendar.OCTOBER, 12, 4), start)
    }

    @Test
    fun `item completed inside window is not pending`() {
        val now = serverTime(2026, Calendar.OCTOBER, 18, 12, 0)
        val item = ChecklistItem(
            id = "id",
            title = "Daily commission",
            cadence = ResetCadence.DAILY,
            lastCompletedAt = serverTime(2026, Calendar.OCTOBER, 18, 7),
        )

        val windowStart = calculator.windowStart(item, now)

        assertFalse(item.isPendingAt(now, windowStart))
    }

    @Test
    fun `item completed before window start is pending again`() {
        val now = serverTime(2026, Calendar.OCTOBER, 18, 12, 0)
        val item = ChecklistItem(
            id = "id",
            title = "Daily commission",
            cadence = ResetCadence.DAILY,
            lastCompletedAt = serverTime(2026, Calendar.OCTOBER, 17, 5),
        )

        val windowStart = calculator.windowStart(item, now)

        assertTrue(item.isPendingAt(now, windowStart))
    }

    @Test
    fun `never completed item is pending`() {
        val now = serverTime(2026, Calendar.OCTOBER, 18, 12, 0)
        val item = ChecklistItem("id", "Resin", ResetCadence.DAILY)

        val windowStart = calculator.windowStart(item, now)

        assertTrue(item.isPendingAt(now, windowStart))
    }
}
