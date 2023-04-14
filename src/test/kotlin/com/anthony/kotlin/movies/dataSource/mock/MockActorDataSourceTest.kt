package com.anthony.kotlin.movies.dataSource.mock

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test


class MockActorDataSourceTest {
    private val mockDataSourse = MockActorDataSource()

    @Test
    fun `should private a collection of actors` () {

        //when
        val actors = mockDataSourse.retrieveActors();


        //then
        assertThat(actors).isNotEmpty
        assertThat(actors.size).isGreaterThanOrEqualTo(3)
    }

    @Test
    fun `should private some mock data` () {

        //when
        val actors = mockDataSourse.retrieveActors()

        //then
        assertThat(actors).allMatch { it.id != 0 }
        assertThat(actors).allMatch { it.name.isNotBlank() }
    }
}