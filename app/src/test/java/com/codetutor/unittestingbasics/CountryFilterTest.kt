package com.codetutor.unittestingbasics

import org.junit.Assert.*
import org.junit.Test

data class Country(val name: String, val driveSide: String)

object CountryFilter {
    fun filterByDriveSide(countries: List<Country>, side: String): List<Country> {
        return countries.filter { it.driveSide == side }
    }
}

class CountryFilterTest {
    @Test
    fun testFilterByDriveSide_left(){
        //Arrange
        val countries = listOf(
            Country("India", "left"),
            Country("USA", "right"),
            Country("UK", "left")
        )
        //Act
        val result = CountryFilter.filterByDriveSide(countries, "left")

        //Assert
        assertEquals(2,result.size)
        assertTrue(result.all { it.driveSide == "left" })
    }
}