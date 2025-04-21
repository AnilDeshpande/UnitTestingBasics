package com.codetutor.unittestingbasics

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryFilter
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)

class CountryFilterNullTests (
    private val driveSide: String,
    private val shouldBeNull: Boolean,
    private val testName: String
) {

    companion object {
        @JvmStatic
        @Parameters
        fun data(): Collection<Array<Any>>{
            return listOf(
                arrayOf("left",true,"Non-matching side should return null element"),
                arrayOf("right",false,"Result should not be null"),
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
    fun testNullScenarios(){
        println("Running Test: $testName")
        val result = CountryFilter.filterByDriveSide(countries,driveSide)
        if(shouldBeNull){
          val nonMatching = result.find { it.driveSide != driveSide }
          Assert.assertNull(nonMatching)
        } else {
            Assert.assertNotNull(result)

        }
    }
}