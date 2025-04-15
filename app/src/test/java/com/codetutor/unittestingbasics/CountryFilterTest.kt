package com.codetutor.unittestingbasics

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryFilter
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

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

    @Test
    fun testFilterByDriveSide_left(){
        //Act
        val result = CountryFilter.filterByDriveSide(countries, "left")
        //Assert
        assertEquals(2,result.size)
    }

    @Test
    fun testFilterByDriveSide_eachEntryIsDriveByLeftSide(){
        //Act
        val result = CountryFilter.filterByDriveSide(countries, "left")
        //Assert
        assertTrue(result.all { it.driveSide == "left" })
    }

    //assertNull
    @Test
    fun `assertNull - test case with no matching side returns empty list but not null`(){
        val result = CountryFilter.filterByDriveSide(countries,"left")
        val nonmatching = result.find { it.driveSide == "right" }
        assertNull(nonmatching)
    }

    @Test
    fun `assertNotNull - result should not be null`() {
        val result = CountryFilter.filterByDriveSide(countries, "right")
        assertNotNull(result)
    }

    //assertSame
    @Test
    fun `assertSame - same reference check (optional test for demo)`() {
        val country = Country("India", "left")
        val ref1 = country
        val ref2 = country
        assertSame(ref1, ref2)
    }

    //assertNotSame
    @Test
    fun `assertNotSame - different object instances should not be same`() {
        val c1 = Country("India", "left")
        val c2 = Country("India", "left")
        assertNotSame(c1, c2)
    }

    //assertThrows
    @Test
    fun `assertThrows - invalid drive side should throw exception`(){
        val exception = assertThrows(IllegalArgumentException::class.java){
            CountryFilter.filterByDriveSide(countries, "middle")
        }
        assertEquals("Invalid drive side: middle", exception.message)
    }

    //assertArrayEquals
    @Test
    fun `assertArrayEquals - convert result names to array and compare`(){
        val result = CountryFilter.filterByDriveSide(countries,"left")
        val countryNames = result.map { it.name }.toTypedArray()
        assertArrayEquals(arrayOf("India","UK"), countryNames)
    }
}