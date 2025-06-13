package com.codetutor.unittestingbasics.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codetutor.unittestingbasics.model.Country

@Database(
    entities = [Country::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryRoomDao
}
