package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.model.Country

interface CountryDAO {
    suspend fun getAll(): List<Country>
    suspend fun insertAll(countries: List<Country>)
}