package com.codetutor.unittestingbasics

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryFilter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.Assert.*


@RunWith(Parameterized::class)
class ParameterizedCountryFilterTest(
    private val driveSide: String,
    private val expectedCount: Int
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Drive side: {0}, Expected count: {1}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf("left", 2),
                arrayOf("right", 1),
                arrayOf("middle", 0) // This will throw if not handled
            )
        }
    }

    private lateinit var countries: List<Country>

    @Before
    fun setup() {
        countries = listOf(
            Country("India", "left"),
            Country("USA", "right"),
            Country("UK", "left")
        )
    }

    @Test
    fun testFilterByDriveSide_parameterized() {
        if (driveSide == "middle") {
            val exception = assertThrows(IllegalArgumentException::class.java) {
                CountryFilter.filterByDriveSide(countries, driveSide)
            }
            assertEquals("Invalid drive side: middle", exception.message)
        } else {
            val result = CountryFilter.filterByDriveSide(countries, driveSide)
            assertEquals(expectedCount, result.size)
        }
    }
}