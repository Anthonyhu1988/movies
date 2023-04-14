package com.anthony.kotlin.movies.controller

import com.anthony.kotlin.movies.model.Actor
import com.anthony.kotlin.movies.model.Movie
import com.anthony.kotlin.movies.service.MovieService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/movies")
class MovieController (private val service: MovieService) {

    @ExceptionHandler(NoSuchElementException ::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException ::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)


    @GetMapping
    fun getMovies(): Collection<Movie> = service.getMovies()

    @GetMapping("/{id}")
    fun getBanks(@PathVariable id: Int)  = service.getMovies(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addMovie(@RequestBody movie : Movie): Movie = service.addMovie(movie)

    @PatchMapping
    fun updateMovie(@RequestBody movie : Movie): Movie = service.updateMovie(movie)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMovie(@PathVariable id: Int) = service.deleteMovie(id)
}