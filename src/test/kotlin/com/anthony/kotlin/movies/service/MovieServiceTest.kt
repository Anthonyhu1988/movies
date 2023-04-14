package com.anthony.kotlin.movies.service

import com.anthony.kotlin.movies.dataSource.MovieDataSource
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MovieServiceTest {
    private val dataSource: MovieDataSource = mockk(relaxed = true)
    private val bankService = MovieService(dataSource)

    @Test
    fun `should call its data source to retrieve movies` () {
        //given


        //when
        bankService.getMovies()

        //then
        verify (exactly = 1) { dataSource.retrieveMovies() }
    }
}