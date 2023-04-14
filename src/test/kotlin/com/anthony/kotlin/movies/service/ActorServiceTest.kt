package com.anthony.kotlin.movies.service

import com.anthony.kotlin.movies.dataSource.ActorDataSource
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ActorServiceTest {
    private val dataSource: ActorDataSource = mockk(relaxed = true)
    private val bankService = ActorService(dataSource)

    @Test
    fun `should call its data source to retrieve actors` () {
        //given


        //when
        bankService.getActors()

        //then
        verify (exactly = 1) { dataSource.retrieveActors() }
    }
}