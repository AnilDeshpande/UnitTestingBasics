package com.codetutor.unittestingbasics

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryFilter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.Assert.*
@RunWith(Parameterized::class)
class CountryFilterNullTests(
    private val driveSide: String,
    private val shouldBeNull: Boolean,
    private val testName: String
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{2}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf("left", true, "Non-matching side should return null element"),
                arrayOf("right", false, "Result should not be null")
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
    fun testNullScenarios() {
        println("Running test: $testName")
        val result = CountryFilter.filterByDriveSide(countries, driveSide)
        if (shouldBeNull) {
            val nonmatching = result.find { it.driveSide != driveSide }
            assertNull(nonmatching)
        } else {
            assertNotNull(result)
        }
    }
}