package com.codetutor.unittestingbasics.di

import android.content.Context
import androidx.room.Room
import com.codetutor.unittestingbasics.database.AppDatabase
import com.codetutor.unittestingbasics.database.CountryRoomDao
import com.codetutor.unittestingbasics.repository.CountryDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "countries.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideCountryDao(db: AppDatabase): CountryDAO = db.countryDao()
}
