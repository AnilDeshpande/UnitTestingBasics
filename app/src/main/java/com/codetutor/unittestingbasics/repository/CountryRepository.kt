package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.model.Country

class CountryRepository(
    private val dataSource: CountryDAO,
    private val service: CountryService
) {
    suspend fun getAll(): List<Country> {
        val local = dataSource.getAll()
        if(local.isNotEmpty()) return local

        return try {
            val remote = service.fetchAll()
            dataSource.insertAll(remote)
            return remote
        }catch (e: Exception){
            local
        }

    }

    suspend fun countriesBySide(side : String): List<Country> =
        try {
            CountryFilter.filterByDriveSide(dataSource.getAll(), side)
        }catch (e: IllegalArgumentException){
            emptyList()
        }


}