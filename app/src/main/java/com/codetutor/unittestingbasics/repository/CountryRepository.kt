package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.model.Country

class CountryRepository(
    private val dataSource: CountryDAO
) {

    suspend fun getAll(): List<Country> = dataSource.getAll()

    suspend fun countriesBySide(side: String): List<Country> =
        CountryFilter.filterByDriveSide(dataSource.getAll(), side)
}