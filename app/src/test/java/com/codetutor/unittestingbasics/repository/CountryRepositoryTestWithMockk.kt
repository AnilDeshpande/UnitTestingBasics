package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.service.CountryService
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CountryRepositoryTestWithMockk {

    // Using MockK annotations to create mocks
    @MockK
    private lateinit var countryDAO: CountryDAO

    @MockK
    private lateinit var countryService: CountryService

    // The class under test
    private lateinit var repository: CountryRepository

    // Test data
    private val indiaCountry = Country("India", "left")
    private val usaCountry = Country("USA", "right")
    private val ukCountry = Country("UK", "left")
    private val allCountries = listOf(indiaCountry, usaCountry, ukCountry)
    private val leftCountries = listOf(indiaCountry, ukCountry)
    private val rightCountries = listOf(usaCountry)
    private val remoteCountries = listOf(
        Country("Japan", "left"),
        Country("Canada", "right")
    )

    @Before
    fun setup() {
        // Initialize the mocks
        MockKAnnotations.init(this)

        // Create the repository with mocked dependencies
        repository = CountryRepository(countryDAO, countryService)
    }

    @After
    fun tearDown() {
        // Clear all recorded calls after each test
        clearAllMocks()
    }

    /**
     * Tests the basic functionality of getAll() method when local data exists.
     * Verifies it correctly returns the full list of countries from the DAO.
     */
    @Test
    fun testGetAllWithLocalData() = runBlocking {
        // Setup - configure mock behavior
        coEvery { countryDAO.getAll() } returns allCountries

        // Action - call the method under test
        val result = repository.getAll()

        // Verify - check results and interactions
        assertEquals(3, result.size)
        assertEquals("India", result[0].name)
        assertEquals("USA", result[1].name)
        assertEquals("UK", result[2].name)

        // Verify the DAO was called but the service was not
        coVerify(exactly = 1) { countryDAO.getAll() }
        coVerify(exactly = 0) { countryService.fetchAll() }
        coVerify(exactly = 0) { countryDAO.insertAll(any()) }
    }

    /**
     * Tests the getAll() method when no local data exists.
     * Verifies it fetches from the service and saves to local storage.
     */
    @Test
    fun testGetAllWithRemoteData() = runBlocking {
        // Setup - empty local data, configure service to return data
        coEvery { countryDAO.getAll() } returns emptyList()
        coEvery { countryService.fetchAll() } returns remoteCountries
        coEvery { countryDAO.insertAll(any()) } just Runs

        // Action - call the method under test
        val result = repository.getAll()

        // Verify - check results and interactions
        assertEquals(2, result.size)
        assertEquals("Japan", result[0].name)
        assertEquals("Canada", result[1].name)

        // Verify the correct methods were called in sequence
        coVerify(exactly = 1) { countryDAO.getAll() }
        coVerify(exactly = 1) { countryService.fetchAll() }
        coVerify(exactly = 1) { countryDAO.insertAll(remoteCountries) }
    }

    /**
     * Tests that countriesBySide() correctly filters and returns only countries
     * that drive on the left side of the road.
     */
    @Test
    fun testGetAllLeft() = runBlocking {
        // Setup - configure mock to return all countries
        coEvery { countryDAO.getAll() } returns allCountries

        // Action - call the method under test
        val result = repository.countriesBySide("left")

        // Verify - check results
        assertEquals(2, result.size)
        assertEquals("India", result[0].name)
        assertEquals("UK", result[1].name)

        // Verify the DAO was called exactly once
        coVerify(exactly = 1) { countryDAO.getAll() }
    }

    /**
     * Tests that countriesBySide() correctly filters and returns only countries
     * that drive on the right side of the road.
     */
    @Test
    fun testGetAllRight() = runBlocking {
        // Setup - configure mock to return all countries
        coEvery { countryDAO.getAll() } returns allCountries

        // Action - call the method under test
        val result = repository.countriesBySide("right")

        // Verify - check results
        assertEquals(1, result.size)
        assertEquals("USA", result[0].name)

        // Verify the DAO was called exactly once
        coVerify(exactly = 1) { countryDAO.getAll() }
    }

    /**
     * Tests the error handling in countriesBySide() when an invalid drive side
     * is provided. Should return an empty list.
     */
    @Test
    fun testGetAllNonMatching() = runBlocking {
        // Setup - configure mock to return all countries
        coEvery { countryDAO.getAll() } returns allCountries

        // Action - call the method under test with invalid side
        val result = repository.countriesBySide("non-matching")

        // Verify - should return empty list due to exception handling
        assertTrue(result.isEmpty())

        // Verify the DAO was called exactly once
        coVerify(exactly = 1) { countryDAO.getAll() }
    }

    /**
     * Tests handling of empty results from the DAO.
     */
    @Test
    fun testEmptyDAO() = runBlocking {
        // Setup - configure mock to return empty list
        coEvery { countryDAO.getAll() } returns emptyList()

        // Action - call the method under test
        val result = repository.countriesBySide("left")

        // Verify - should return empty list
        assertTrue(result.isEmpty())

        // Verify the DAO was called exactly once
        coVerify(exactly = 1) { countryDAO.getAll() }
    }
}
