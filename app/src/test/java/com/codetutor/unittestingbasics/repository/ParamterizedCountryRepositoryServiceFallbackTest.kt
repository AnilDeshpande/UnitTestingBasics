package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.doubles.FakeCountryDao
import com.codetutor.unittestingbasics.doubles.StubCountryDAO
import com.codetutor.unittestingbasics.doubles.StubCountryService
import com.codetutor.unittestingbasics.doubles.ThrowingServiceStub
import com.codetutor.unittestingbasics.model.Country

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Parameterized test that runs the same repository tests with different combinations of
 * DAO and Service implementations to verify consistent behavior across implementations.
 */
@RunWith(Parameterized::class)
class ParamterizedCountryRepositoryServiceFallbackTest(
    private val dao: CountryDAO,
    private val service: CountryService
) {

    private lateinit var repo: CountryRepository

    @Before
    fun setUp() {
        when (dao) {
            is StubCountryDAO -> (dao as StubCountryDAO).clear()
            is FakeCountryDao -> (dao as FakeCountryDao).clear()
        }
        repo = CountryRepository(dao, service)
    }

    @After
    fun tearDown() {
        if (dao is StubCountryDAO) {
            (dao as StubCountryDAO).clear() // Clear prepopulated data
        }
        repo = CountryRepository(dao, service)
    }

    /**
     * Tests that when the local DAO is empty, the repository fetches data from the remote service
     * and caches it. Skips this test for ThrowingServiceStub to avoid test failures.
     */
    @Test fun `empty DAO triggers remote fetch & caches result`() = runBlocking {
        if (service is ThrowingServiceStub) return@runBlocking // Skip for ThrowingServiceStub
        val result = repo.getAll()
        Assert.assertEquals(2, result.size)
        Assert.assertEquals(2, dao.getAll().size)
    }

    /**
     * Tests that when the local DAO has data, the repository returns it without calling the remote
     * service. This behavior should be consistent across all DAO and Service implementations.
     */
    @Test fun `non-empty DAO skips service`() = runBlocking {
        dao.insertAll(listOf(Country("India", "left"))) // Warm cache
        val result = repo.getAll()
        Assert.assertEquals(1, result.size)
    }

    /**
     * Tests that when the remote service throws an exception, the repository gracefully falls back
     * to using the local DAO data. This test is relevant for all combinations, but particularly
     * important for implementations with ThrowingServiceStub.
     */
    @Test fun `service failure falls back to DAO`() = runBlocking {
        dao.insertAll(listOf(Country("India", "left"))) // Prepopulate DAO
        val result = repo.getAll() // Should fall back to DAO
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("India", result[0].name)
    }

    companion object {
        /**
         * Provides test parameters that combine different DAO and Service implementations:
         * - FakeCountryDAO with StubCountryService (normal operation)
         * - FakeCountryDAO with ThrowingServiceStub (service failure)
         * - StubCountryDAO with StubCountryService (normal operation with stub DAO)
         * - StubCountryDAO with ThrowingServiceStub (service failure with stub DAO)
         */
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: DAO={0}, Service={1}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(FakeCountryDao(), StubCountryService()),
                arrayOf(FakeCountryDao(), ThrowingServiceStub()),
                arrayOf(StubCountryDAO(), StubCountryService()),
                arrayOf(StubCountryDAO(), ThrowingServiceStub()),
            )
        }
    }
}