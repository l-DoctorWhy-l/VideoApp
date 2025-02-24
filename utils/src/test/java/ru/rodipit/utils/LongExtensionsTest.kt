package ru.rodipit.utils

import org.junit.Test

import org.junit.Assert.*

class LongExtensionsTest {

    @Test
    fun `Long to formatted time duration test`() {
        assertTrue("00:16:40" == 1000L.toFormattedTimeDuration())
        assertTrue("00:00:00" == 0L.toFormattedTimeDuration())
        assertTrue("00:00:01" == 1L.toFormattedTimeDuration())
    }

}