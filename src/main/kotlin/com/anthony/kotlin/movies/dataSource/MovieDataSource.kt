package com.anthony.kotlin.movies.dataSource

import com.anthony.kotlin.movies.model.Movie

interface MovieDataSource {

    fun retrieveMovies(): Collection<Movie>

    fun retrieveMovie(id: Int): Movie

    fun createMovie(movie: Movie): Movie

    fun updateMovie(movie: Movie): Movie

    fun deleteMovie(id: Int)

}