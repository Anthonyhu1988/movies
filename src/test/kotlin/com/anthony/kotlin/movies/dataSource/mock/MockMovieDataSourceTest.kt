package com.anthony.kotlin.movies.dataSource.mock

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MockMovieDataSourceTest {
    private val mockDataSourse = MockMovieDataSource()

    @Test
    fun `should private a collection of movies` () {

        //when
        val movies = mockDataSourse.retrieveMovies();


        //then
        Assertions.assertThat(movies).isNotEmpty
        Assertions.assertThat(movies.size).isGreaterThanOrEqualTo(3)
    }

    @Test
    fun `should private some mock data` () {

        //when
        val movies = mockDataSourse.retrieveMovies()

        //then
        Assertions.assertThat(movies).allMatch { it.id != 0 }
        Assertions.assertThat(movies).allMatch { it.title.isNotBlank() }
        Assertions.assertThat(movies).allMatch { it.releaseDate.isNotBlank() }
        Assertions.assertThat(movies).allMatch { it.actors.size >= 1}
    }
}