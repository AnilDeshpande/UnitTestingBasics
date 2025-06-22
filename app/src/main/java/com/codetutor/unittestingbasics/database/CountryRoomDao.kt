package com.codetutor.unittestingbasics.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryDAO

@Dao
interface CountryRoomDao : CountryDAO {

    @Query("SELECT * FROM countries")
    override suspend fun getAll(): List<Country>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertAll(countries: List<Country>)
}