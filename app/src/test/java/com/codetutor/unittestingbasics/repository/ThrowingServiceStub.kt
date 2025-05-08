package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.service.CountryService
import java.io.IOException

class ThrowingServiceStub : CountryService {
    override suspend fun fetchAll(): List<Country> =
        throw IOException("boom")
}