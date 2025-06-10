package com.codetutor.unittestingbasics.doubles

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryService
import java.io.IOException

class ThrowingServiceStub : CountryService {
    override suspend fun fetchAll(): List<Country> {
        throw IOException("Boom")
    }
}