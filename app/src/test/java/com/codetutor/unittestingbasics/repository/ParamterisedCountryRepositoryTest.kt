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

@RunWith(Parameterized::class)
class ParamterisedCountryRepositoryTest(
    private val drivingSide: String,
    private val expectedCountries: List<Country>
) {

    private lateinit var repository: CountryRepository
    private lateinit var countryDAO: FakeCountryDAO
    private lateinit var countryService: CountryService

    companion object {
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
