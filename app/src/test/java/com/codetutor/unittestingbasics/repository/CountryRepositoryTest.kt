package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.doubles.dao.FakeCountryDAO
import com.codetutor.unittestingbasics.doubles.service.StubCountryService
import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.service.CountryService
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class CountryRepositoryTest {

    private lateinit var countryDAO : CountryDAO
    private lateinit var repository: CountryRepository
    private lateinit var countryService: CountryService

    @Before
    fun setup(){
        //countryDAO = StubCountryDAO()
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
     * Tests the basic functionality of getAll() method to verify it correctly
     * returns the full list of countries from the DAO.
     */
    @Test
    fun testGetAll() = runBlocking {
        val countries = repository.getAll()
        assert(countries.isNotEmpty())
        assert(countries.size == 3)
        assert(countries[0].name == "India")
        assert(countries[1].name == "USA")
        assert(countries[2].name == "UK")
    }

    /**
     * Tests that countriesBySide() correctly filters and returns only countries
     * that drive on the left side of the road.
     */
    @Test
    fun testGetAllLeft() = runBlocking {
        val countries = repository.countriesBySide("left")
        assert(countries.isNotEmpty())
        assert(countries.size == 2)
        assert(countries[0].name == "India")
        assert(countries[1].name == "UK")
    }

    /**
     * Tests that countriesBySide() correctly filters and returns only countries
     * that drive on the right side of the road.
     */
    @Test
    fun testGetAllRight() = runBlocking {
        val countries = repository.countriesBySide("right")
        assert(countries.isNotEmpty())
        assert(countries.size == 1)
        assert(countries[0].name == "USA")
    }

    /**
     * Tests the error handling in countriesBySide() when an invalid drive side
     * is provided. Should return an empty list.
     */
    @Test
    fun testGetAllNonMatching() = runBlocking {
        val countries = repository.countriesBySide("non-matching")
        assert(countries.isEmpty())
    }

    /**
     * Tests filtering countries with "left" drive side to ensure the
     * correct subset is returned. Validates filter behavior and data integrity.
     */
    @Test
    fun testGetAllNull() = runBlocking {
        val countries = repository.countriesBySide("left")
        assert(countries.isNotEmpty())
        assert(countries.size == 2)
        assert(countries[0].name == "India")
        assert(countries[1].name == "UK")
    }
}