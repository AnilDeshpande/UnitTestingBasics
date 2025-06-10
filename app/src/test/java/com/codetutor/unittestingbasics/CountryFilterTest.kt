package com.codetutor.unittestingbasics

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryFilter
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Test class that verifies the functionality of CountryFilter with various
 * assertion types and filtering scenarios.
 */
class CountryFilterTest {

    private var countries = listOf<Country>()

    @Before
    fun setup(){
         countries = listOf(
            Country("India", "left"),
            Country("USA", "right"),
            Country("UK", "left")
        )
    }

    @After
    fun tearDown(){
        countries = listOf<Country>()
    }

    /**
     * Tests that filterByDriveSide correctly returns the expected number of countries
     * when filtering for left-side driving countries.
     */
    @Test
    fun testFilterByDriveSide_left(){

        //Act
        val result = CountryFilter.filterByDriveSide(countries, "left")

        //Assert
        assertEquals(2,result.size)
    }

    /**
     * Tests that filterByDriveSide ensures all returned countries actually drive
     * on the left side by using the all() higher-order function.
     */
    @Test
    fun testFilterByDriveSide_eachEntryIsDriveByLeftSide(){

        //Act
        val result = CountryFilter.filterByDriveSide(countries, "left")

        //Assert
        assertTrue(result.all { it.driveSide == "left" })
    }

    /**
     * Tests that when searching for left-side countries, no right-side
     * country can be found in the results (using assertNull).
     */
    @Test
    fun `assertNull - test case with no matching side returns empty list but not null`(){
        val result = CountryFilter.filterByDriveSide(countries,"left")
        val nonmatching = result.find { it.driveSide == "right" }
        assertNull(nonmatching)
    }

    /**
     * Tests that the filter result is never null, even when filtering
     * returns countries (using assertNotNull).
     */
    @Test
    fun `assertNotNull - result should not be null`() {
        val result = CountryFilter.filterByDriveSide(countries, "right")
        assertNotNull(result)
    }

    /**
     * Demonstrates assertSame by verifying that two references to the same
     * Country object are indeed the same instance.
     */
    @Test
    fun `assertSame - same reference check (optional test for demo)`() {
        val country = Country("India", "left")
        val ref1 = country
        val ref2 = country
        assertSame(ref1, ref2)
    }

    /**
     * Demonstrates assertNotSame by verifying that two Country objects with
     * identical properties are still different instances.
     */
    @Test
    fun `assertNotSame - different object instances should not be same`() {
        val c1 = Country("India", "left")
        val c2 = Country("India", "left")
        assertNotSame(c1, c2)
    }

    /**
     * Tests that the filter throws an IllegalArgumentException with the correct
     * error message when an invalid drive side is provided.
     */
    @Test
    fun `assertThrows - invalid drive side should throw exception`(){
        val exception = assertThrows(IllegalArgumentException::class.java){
            CountryFilter.filterByDriveSide(countries, "middle")
        }

        assertEquals("Invalid drive side: middle", exception.message)
    }

    /**
     * Tests that the names of filtered countries match the expected values
     * by converting to arrays and using assertArrayEquals.
     */
    @Test
    fun `assertArrayEquals - convert result names to array and compare`(){
        val result = CountryFilter.filterByDriveSide(countries,"left")
        val countryNames = result.map { it.name }.toTypedArray()
        assertArrayEquals(arrayOf("India","UK"), countryNames)
    }
}