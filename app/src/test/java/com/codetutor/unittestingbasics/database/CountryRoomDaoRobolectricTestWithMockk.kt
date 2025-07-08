        Assert.assertEquals(1, result.size)
        dao.insertAll(countries)

        // Assert - Verify the exact objects passed to insertAll
        Assert.assertEquals(2, slot.captured.size)
        Assert.assertEquals("Spain", slot.captured[0].name)
        Assert.assertEquals("India", slot.captured[1].name)

        coVerify(exactly = 1) { dao.insertAll(countries) }
    }
}
