package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.doubles.FakeCountryDao
import com.codetutor.unittestingbasics.doubles.StubCountryDAO
import com.codetutor.unittestingbasics.doubles.StubCountryService
import com.codetutor.unittestingbasics.model.Country
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CountryRepositoryTest {
    private lateinit var countryDAO : CountryDAO
    private lateinit var repository: CountryRepository
    private lateinit var service : CountryService

    @Before
    fun setup(){
        countryDAO = FakeCountryDao(
            listOf(
                Country("India", "left"),
                Country("USA", "right"),
                Country("UK", "left")
            )
        )
        service = StubCountryService()
        repository = CountryRepository(countryDAO, service)
    }

    @Test
    fun testGetAll() = runBlocking {
        val countries = repository.getAll()
        assert(countries.isNotEmpty())
        assert(countries.size == 3)
        assert(countries[0].name == "India")
        assert(countries[1].name == "USA")
        assert(countries[2].name == "UK")
    }

    @Test
    fun testGetAllLeft() = runBlocking {
        val countries = repository.countriesBySide("left")
        assert(countries.isNotEmpty())
        assert(countries.size == 2)
        assert(countries[0].name == "India")
        assert(countries[1].name == "UK")
    }

    @Test
    fun testGetAllRight() = runBlocking {
        val countries = repository.countriesBySide("right")
        assert(countries.isNotEmpty())
        assert(countries.size == 1)
        assert(countries[0].name == "USA")
    }

    @Test
    fun testGetAllNonMatching() = runBlocking {
        val countries = repository.countriesBySide("non-matching")
        assert(countries.isEmpty())
    }

    @Test
    fun testGetAllNull() = runBlocking {
        val countries = repository.countriesBySide("left")
        assert(countries.isNotEmpty())
        assert(countries.size == 2)
        assert(countries[0].name == "India")
        assert(countries[1].name == "UK")
    }
}