package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.doubles.FakeCountryDao
import com.codetutor.unittestingbasics.model.Country
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ParamterisedCountryRepositoryTest(
    private val drivingSide: String,
    private val expectedCountries: List<Country>
) {

    private lateinit var repository: CountryRepository
    private lateinit var countryDAO: FakeCountryDao

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Driving side: {0}")
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf(
                "left",
                listOf(
                    Country("India", "left"),
                    Country("UK", "left")
                )
            ),
            arrayOf(
                "right",
                listOf(
                    Country("USA", "right")
                )
            ),
            arrayOf(
                "non-existing",
                emptyList<Country>()
            )
        )
    }

    @Before
    fun setup() {
        countryDAO = FakeCountryDao(
            listOf(
                Country("India", "left"),
                Country("USA", "right"),
                Country("UK", "left")
            )
        )
        repository = CountryRepository(countryDAO)
    }

    @Test
    fun testCountriesByBrivingSide() = runBlocking {
        // When: we request countries by driving side
        val result = repository.countriesBySide(drivingSide)

        // Then: the result should match the expected countries
        Assert.assertEquals(expectedCountries.size, result.size)
        expectedCountries.forEachIndexed { index, expectedCountry ->
            Assert.assertEquals(expectedCountry.name, result[index].name)
            Assert.assertEquals(expectedCountry.driveSide, result[index].driveSide)
        }
    }
}