package com.codetutor.unittestingbasics

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.codetutor.unittestingbasics.database.AppDatabase
import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryDAO
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for Room database.
 * This test class verifies the correct functioning of the Country DAO (Data Access Object)
 * with a real SQLite database using Room's in-memory database implementation.
 */
@RunWith(AndroidJUnit4::class)
class CountryDaoInstrumentedTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var dao: CountryDAO

    /**
     * Sets up a fresh in-memory database before each test.
     * Using in-memory database ensures tests are isolated and don't affect real device storage.
     */
    @Before
    fun setUp() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()    // safe in tests
            .build()
        dao = db.countryDao()
    }

    /**
     * Closes the database after each test to release resources and
     * prevent memory leaks between test executions.
     */
    @After
    fun tearDown() = db.close()

    /**
     * Tests the basic insert and read operations of the DAO.
     * Verifies that a country can be inserted and retrieved with all properties intact.
     */
    @Test
    fun insert_and_read() = runTest {
        val seed = listOf(Country("India", "left"))
        dao.insertAll(seed)
        val fromDb = dao.getAll()
        Assert.assertEquals(1, fromDb.size)
        Assert.assertEquals("left", fromDb[0].driveSide)
    }
    
    /**
     * Tests inserting and retrieving multiple countries simultaneously.
     * Verifies that all countries are correctly stored in the database.
     */
    @Test
    fun insert_multiple_and_read() = runTest {
        val countries = listOf(
            Country("India", "left"),
            Country("United States", "right"),
            Country("United Kingdom", "left")
        )
        dao.insertAll(countries)
        val fromDb = dao.getAll()
        Assert.assertEquals(3, fromDb.size)
        Assert.assertEquals("United States", fromDb.find { it.driveSide == "right" }?.name)
    }
    
    /**
     * Tests replacing existing countries in the database.
     * Verifies that inserting a country with an existing primary key updates the record.
     */
    @Test
    fun replace_existing_countries() = runTest {
        // Insert initial data
        val initial = listOf(Country("India", "left"))
        dao.insertAll(initial)
        
        // Replace with new data (same primary key)
        val updated = listOf(Country("India", "right"))
        dao.insertAll(updated)
        
        // Verify the data was replaced
        val fromDb = dao.getAll()
        Assert.assertEquals(1, fromDb.size)
        Assert.assertEquals("right", fromDb[0].driveSide)
    }
}
