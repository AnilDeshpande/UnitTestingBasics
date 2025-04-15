package com.codetutor.unittestingbasics

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryFilter
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ParameterizedCountryFilterTest(
    private val driveSide: String,
    private val expectedCount: Int
){
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Drive side : {0}, expected count : {1}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf("left",2),
                arrayOf("right", 1),
                arrayOf("middle",0)
            )
        }
    }
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
    fun testFilterByDriveSide_paramterized(){
        if(driveSide == "middle"){
            val exception = assertThrows(IllegalArgumentException::class.java){
                CountryFilter.filterByDriveSide(countries, "middle")
            }
            assertEquals("Invalid drive side: middle", exception.message)
        }else {
            val result = CountryFilter.filterByDriveSide(countries,driveSide)
            Assert.assertEquals(expectedCount, result.size)
        }

    }
}