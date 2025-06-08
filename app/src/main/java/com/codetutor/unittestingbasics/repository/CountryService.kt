package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.model.Country

interface CountryService {
    suspend fun fetchAll(): List<Country>
}