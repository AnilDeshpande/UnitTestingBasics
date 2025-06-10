package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.doubles.FakeCountryDao
import com.codetutor.unittestingbasics.doubles.StubCountryService
import com.codetutor.unittestingbasics.doubles.ThrowingServiceStub
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

    @Test
    fun `empty DAO triggers remote fetch & caches result`() = runBlocking {
        val result = repo.getAll()
        Assert.assertEquals(2, result.size)
        Assert.assertEquals(2, dao.getAll().size)
    }

    @Test fun `non-empty DAO skips service`() = runBlocking {
        dao.insertAll(listOf(Country("India","left")))                // warm cache
        val result = repo.getAll()
        Assert.assertEquals(1, result.size)
    }

    @Test
    fun `service failure falls back to DAO`() = runBlocking {
        dao.insertAll(listOf(Country("India","left")))
        repo = CountryRepository(dao, ThrowingServiceStub())

        val result = repo.getAll()
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("India", result[0].name)
    }

    @Test
    fun `service failure falls back to DAO with prepopulated data`() = runBlocking {
        repo = CountryRepository(dao, ThrowingServiceStub())
        dao.insertAll(listOf(Country("India", "left")))

        val result = repo.getAll() // Should fall back to DAO
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("India", result[0].name)

    }

}