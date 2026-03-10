package com.morarfilip.githubexplorer.core.common

import org.junit.Assert.assertEquals
import org.junit.Test


class DateFormatterTest {
    @Test
    fun parsesIsoDateToHumanReadableFormat() {
        val input = "2026-03-09T15:54:11Z"
        val expected = "Mar 09, 2026 - 03:54 PM"
        assertEquals(expected, DateFormatter.formatIsoDate(input))
    }

    @Test
    fun returnsOriginalStringOnInvalidInput() {
        val input = "bad-date-format"
        assertEquals(input, DateFormatter.formatIsoDate(input))
    }
}