package com.codetutor.unittestingbasics.service

import com.codetutor.unittestingbasics.model.Country

interface CountryService {
    suspend fun fetchAll(): List<Country>
}