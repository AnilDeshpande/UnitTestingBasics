package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.service.CountryService

class CountryRepository(
    private val dataSource: CountryDAO,
    private val service: CountryService
) {

    suspend fun getAll(): List<Country> {
        val local = dataSource.getAll()
        if (local.isNotEmpty()) return local

        val remote = service.fetchAll()
        dataSource.insertAll(remote)
        return remote
    }

    /*suspend fun countriesBySide(side: String): List<Country> =
        CountryFilter.filterByDriveSide(dataSource.getAll(), side)*/

    suspend fun countriesBySide(side: String): List<Country> {
        return try {
            CountryFilter.filterByDriveSide(dataSource.getAll(), side)
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }
}