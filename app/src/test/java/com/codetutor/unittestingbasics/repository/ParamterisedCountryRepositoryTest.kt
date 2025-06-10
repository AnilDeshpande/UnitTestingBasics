package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.doubles.dao.FakeCountryDAO
import com.codetutor.unittestingbasics.doubles.service.StubCountryService
import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.service.CountryService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

/**
 * Parameterized test class that verifies the countriesBySide() method with different
 * driving side inputs and expected country lists. Tests the filtering logic for various scenarios.
 */
@RunWith(Parameterized::class)
class ParamterisedCountryRepositoryTest(
    private val drivingSide: String,
    private val expectedCountries: List<Country>
) {

    private lateinit var repository: CountryRepository
    private lateinit var countryDAO: FakeCountryDAO
    private lateinit var countryService: CountryService

    companion object {
        /**
         * Provides test parameters with different driving sides and corresponding expected countries:
         * - "left" side with India and UK
         * - "right" side with USA only
         * - "non-existing" side with empty list (error handling case)
         */
        @JvmStatic
        @Parameters(name = "Driving side: {0}")
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
        countryDAO = FakeCountryDAO(
            listOf(
                Country("India", "left"),
                Country("USA", "right"),
                Country("UK", "left")
            )
        )
        countryService = StubCountryService()
        repository = CountryRepository(countryDAO, countryService)
    }

    /**
     * Tests the countriesBySide() method with different driving side parameters to verify:
     * 1. Left-side driving countries are correctly filtered
     * 2. Right-side driving countries are correctly filtered
     * 3. Invalid driving sides return empty lists (error handling)
     */
    @Test
    fun `test countries by driving side`() = runBlocking {
        // When: we request countries by driving side
        val result = repository.countriesBySide(drivingSide)

        // Then: the result should match the expected countries
        assertEquals(expectedCountries.size, result.size)
        expectedCountries.forEachIndexed { index, expectedCountry ->
            assertEquals(expectedCountry.name, result[index].name)
            assertEquals(expectedCountry.driveSide, result[index].driveSide)
        }
    }
}
