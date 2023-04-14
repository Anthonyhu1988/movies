package com.anthony.kotlin.movies.service

import com.anthony.kotlin.movies.dataSource.MovieDataSource
import com.anthony.kotlin.movies.model.Movie
import org.springframework.stereotype.Service

@Service
class MovieService (private val dataSource: MovieDataSource) {

    fun getMovies(): Collection<Movie> = dataSource.retrieveMovies()

    fun getMovies(id: Int): Movie = dataSource.retrieveMovie(id)

    fun addMovie(movie: Movie): Movie = dataSource.createMovie(movie)

    fun updateMovie(movie: Movie): Movie  = dataSource.updateMovie(movie)

    fun deleteMovie(id: Int): Unit = dataSource.deleteMovie(id)

}