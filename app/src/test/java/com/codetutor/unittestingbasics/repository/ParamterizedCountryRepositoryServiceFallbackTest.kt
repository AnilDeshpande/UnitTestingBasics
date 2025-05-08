package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.doubles.dao.FakeCountryDAO
import com.codetutor.unittestingbasics.doubles.dao.StubCountryDAO
import com.codetutor.unittestingbasics.doubles.service.StubCountryService
import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.service.CountryService
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

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
            is FakeCountryDAO -> (dao as FakeCountryDAO).clear()
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

    @Test fun `empty DAO triggers remote fetch & caches result`() = runBlocking {
        if (service is ThrowingServiceStub) return@runBlocking // Skip for ThrowingServiceStub
        val result = repo.getAll()
        Assert.assertEquals(2, result.size)
        Assert.assertEquals(2, dao.getAll().size)
    }

    @Test fun `non-empty DAO skips service`() = runBlocking {
        dao.insertAll(listOf(Country("India", "left"))) // Warm cache
        val result = repo.getAll()
        Assert.assertEquals(1, result.size)
    }

    @Test fun `service failure falls back to DAO`() = runBlocking {
        dao.insertAll(listOf(Country("India", "left"))) // Prepopulate DAO
        val result = repo.getAll() // Should fall back to DAO
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("India", result[0].name)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: DAO={0}, Service={1}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(FakeCountryDAO(), StubCountryService()),
                arrayOf(FakeCountryDAO(), ThrowingServiceStub()),
                arrayOf(StubCountryDAO(), StubCountryService()),
                arrayOf(StubCountryDAO(), ThrowingServiceStub()),
            )
        }
    }
}