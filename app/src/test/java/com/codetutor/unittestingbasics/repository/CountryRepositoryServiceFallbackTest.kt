package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.doubles.FakeCountryDao
import com.codetutor.unittestingbasics.doubles.StubCountryService
import com.codetutor.unittestingbasics.model.Country
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CountryRepositoryServiceFallbackTest {
    private lateinit var dao: CountryDAO
    private lateinit var service: CountryService
    private lateinit var repo: CountryRepository
    
    @Before
    fun setUp(){
        dao = FakeCountryDao()
        service = StubCountryService()
        repo = CountryRepository(dao, service)
    }
}