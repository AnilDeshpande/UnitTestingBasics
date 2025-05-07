package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.doubles.StubCountryDAO
import com.codetutor.unittestingbasics.model.Country
import org.junit.Before

class CountryRepositoryTest {
    private lateinit var countryDAO : CountryDAO
    private lateinit var repository: CountryRepository

    @Before
    fun setup(){
        countryDAO = StubCountryDAO()
        repository = CountryRepository(countryDAO)
    }

    //Any number of test cases
}