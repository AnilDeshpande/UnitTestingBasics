package com.codetutor.unittestingbasics

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.codetutor.unittestingbasics.database.AppDatabase
import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.model.DriveSide
import com.codetutor.unittestingbasics.repository.CountryDAO
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class CountryDaoInstrumentedTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var dao: CountryDAO

    @Before
    fun setUp() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()    // safe in tests
            .build()
        dao = db.countryDao()
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun insert_and_read() = runTest {
        val seed = listOf(Country("India", DriveSide.LEFT))
        dao.insertAll(seed)
        val fromDb = dao.getAll()
        assertEquals(1, fromDb.size)
        assertEquals(DriveSide.LEFT, fromDb[0].driveSide)
    }
    
    @Test
    fun insert_multiple_and_read() = runTest {
        val countries = listOf(
            Country("India", DriveSide.LEFT),
            Country("United States", DriveSide.RIGHT),
            Country("United Kingdom", DriveSide.LEFT)
        )
        dao.insertAll(countries)
        val fromDb = dao.getAll()
        assertEquals(3, fromDb.size)
        assertEquals("United States", fromDb.find { it.driveSide == DriveSide.RIGHT }?.name)
    }
    
    @Test
    fun replace_existing_countries() = runTest {
        // Insert initial data
        val initial = listOf(Country("India", DriveSide.LEFT))
        dao.insertAll(initial)
        
        // Replace with new data (same primary key)
        val updated = listOf(Country("India", DriveSide.RIGHT))
        dao.insertAll(updated)
        
        // Verify the data was replaced
        val fromDb = dao.getAll()
        assertEquals(1, fromDb.size)
        assertEquals(DriveSide.RIGHT, fromDb[0].driveSide)
    }
}
