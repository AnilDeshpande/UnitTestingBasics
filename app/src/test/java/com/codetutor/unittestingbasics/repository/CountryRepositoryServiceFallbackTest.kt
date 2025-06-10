package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.doubles.dao.FakeCountryDAO
import com.codetutor.unittestingbasics.doubles.service.StubCountryService
import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.service.CountryService
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CountryRepositoryServiceFallbackTest {

    private lateinit var dao: FakeCountryDAO
    private lateinit var service: CountryService
    private lateinit var repo: CountryRepository

    @Before fun setUp() {
        dao = FakeCountryDAO()
        service = StubCountryService()
        repo = CountryRepository(dao, service)
    }

    /**
     * Tests that when the local DAO is empty, the repository fetches data from the remote service
     * and stores it in the local cache. Verifies both the returned data and cached data size.
     */
    @Test fun `empty DAO triggers remote fetch & caches result`() = runBlocking {
        val result = repo.getAll()
        Assert.assertEquals(2, result.size)
        Assert.assertEquals(2, dao.getAll().size)
    }

    /**
     * Tests that when the local DAO has data, the repository returns it without calling the remote
     * service. Verifies by checking that only the local data is returned.
     */
    @Test fun `non-empty DAO skips service`() = runBlocking {
        dao.insert(Country("India","left"))                // warm cache
        val result = repo.getAll()
        Assert.assertEquals(1, result.size)
    }

    /**
     * Tests that when the remote service throws an exception, the repository gracefully falls back
     * to using the local DAO data. Verifies by checking the data returned matches the local cache.
     */
    @Test fun `service failure falls back to DAO`() = runBlocking {
        dao.insert(Country("India", "left")) // Prepopulate DAO
        repo = CountryRepository(dao, ThrowingServiceStub()) // Use ThrowingServiceStub

        val result = repo.getAll() // Should fall back to DAO
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("India", result[0].name)
    }

    /**
     * Tests that service failure fallback works correctly even when the DAO is prepopulated
     * after creating the repository. Verifies the repository still accesses the latest DAO data.
     */
    @Test fun `service failure falls back to DAO with prepopulated data`() = runBlocking {
        repo = CountryRepository(dao, ThrowingServiceStub()) // Replace the service with ThrowingServiceStub
        dao.insert(Country("India", "left")) // Prepopulate DAO
        val result = repo.getAll() // Should fall back to DAO
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("India", result[0].name)
    }
}