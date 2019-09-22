package com.chesire.malime.flow.series.detail

import android.text.Spanned
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RangeInputFilterTests {
    @Test
    fun `filter with max of 0 always returns null`() {
        val mockSpanned = mockk<Spanned>() {
            every { this@mockk.toString() } returns "10"
        }

        val classUnderTest = RangeInputFilter(0)

        assertNull(classUnderTest.filter("0", 0, 0, mockSpanned, 0, 0))
    }

    @Test
    fun `filter new value less than max returns null`() {
        val mockSpanned = mockk<Spanned>() {
            every { this@mockk.toString() } returns "40"
        }

        val classUnderTest = RangeInputFilter(50)

        assertNull(classUnderTest.filter("0", 0, 0, mockSpanned, 0, 0))
    }

    @Test
    fun `filter new value equal to max returns null`() {
        val mockSpanned = mockk<Spanned>() {
            every { this@mockk.toString() } returns "50"
        }

        val classUnderTest = RangeInputFilter(50)

        assertNull(classUnderTest.filter("0", 0, 0, mockSpanned, 0, 0))
    }

    @Test
    fun `filter new value greater than max returns empty string`() {
        val mockSpanned = mockk<Spanned>() {
            every { this@mockk.toString() } returns "51"
        }

        val classUnderTest = RangeInputFilter(50)

        assertEquals("", classUnderTest.filter("0", 0, 0, mockSpanned, 0, 0))
    }

    @Test
    fun `null source input returns empty string`() {
        val mockSpanned = mockk<Spanned>() {
            every { this@mockk.toString() } returns "0"
        }

        val classUnderTest = RangeInputFilter(50)

        assertEquals("", classUnderTest.filter(null, 0, 0, mockSpanned, 0, 0))
    }

    @Test
    fun `null dest input returns empty string`() {
        val classUnderTest = RangeInputFilter(50)

        assertEquals("", classUnderTest.filter("50", 0, 0, null, 0, 0))
    }

    @Test
    fun `invalid inputs returns empty string`() {
        val mockSpanned = mockk<Spanned>() {
            every { this@mockk.toString() } returns "0"
        }

        val classUnderTest = RangeInputFilter(50)

        assertEquals("", classUnderTest.filter(null, -1, -2, mockSpanned, -3, -4))
    }
}
