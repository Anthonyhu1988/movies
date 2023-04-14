package com.anthony.kotlin.movies.dataSource.mock

import com.anthony.kotlin.movies.dataSource.MovieDataSource
import com.anthony.kotlin.movies.model.Actor
import com.anthony.kotlin.movies.model.Movie
import org.springframework.stereotype.Repository
import java.lang.IllegalArgumentException

@Repository
class MockMovieDataSource : MovieDataSource {

    val actors1 = mutableListOf(
        Actor(1, "Robert Downey Jr."),
        Actor(2, "Gwyneth Paltrow"),
        Actor(3, "Jeff Bridges")
    )

    val actors2 = mutableListOf(
        Actor(4, "Mark Wahlberg"),
        Actor(5, "Taylor Kitsch"),
        Actor(6, "Ben Foster")
    )

    val actors3 = mutableListOf(
        Actor(7, "Robert Downey Jr."),
        Actor(8, "Jude Law"),
        Actor(9, "Mark Strong")
    )

    val movies = mutableListOf(
        Movie(1, "Iron Man", "2008-05-02", actors1),
        Movie(2, "Lone Survivor", "2014-01-10", actors2),
        Movie(3, "Sherlock Holmes", "2009-12-25", actors3)
    )


    override fun retrieveMovies(): Collection<Movie> = movies

    override fun retrieveMovie(id: Int): Movie = movies.firstOrNull { it.id == id }
        ?: throw NoSuchElementException("Could not find a Movie with ID number of $id")

    override fun createMovie(movie: Movie): Movie {
        if (movie.title.isEmpty() || movie.releaseDate.isEmpty()) {
            throw IllegalArgumentException("Movie's title or release date can not be empty")
        }

        var cnt = 0;
        if (movie.actors.size > 0) {
            if (movie.actors.any { !it.name.isEmpty() }) cnt++
        }

        if (movie.actors.isEmpty() || cnt == 0) {
            throw IllegalArgumentException("Movie's actors list must has at lease 1 actor")
        }

        var set = HashSet<String>()
        val list = listOf<String>()
        for (actor in movie.actors) {
            set.add(actor.name)
        }
        if (!movie.actors.isEmpty() && set.size != movie.actors.size) {
            throw IllegalArgumentException("movie's actors list has duplications")
        }

        if (movie.id < 0) {
            throw IllegalArgumentException("movie's id must be a number smaller than 0")
        }

        if (movies.any { (it.title + " " + it.releaseDate) == (movie.title + " " + movie.releaseDate)}) {
            throw IllegalArgumentException("movie with name of ${movie.title} with release data of ${movie.releaseDate}  already exist ")
        }

        if (movies.any { it.id == movie.id}) {
            throw IllegalArgumentException("movie with id of ${movie.id} already exist ")
        }

        movies.add(movie)
        return movie
    }

//    override fun updateMovie(movie: Movie): Movie {
//        val currentMovie = movies.firstOrNull { (it.title + " " + it.releaseDate) == (movie.title + " " + movie.releaseDate) && it.id == movie.id }
//            ?: throw NoSuchElementException("Could not find a movie with name of ${movie.title} with release data of ${movie.releaseDate} and id of ${movie.id}" )
//        movies.remove(currentMovie)
//        movies.add(movie)
//        return movie
//    }

    override fun updateMovie(movie: Movie): Movie {
        val currentMovie = movies.firstOrNull { it.id == movie.id }
            ?: throw NoSuchElementException("Could not find a movie with id of ${movie.id}" )
        movies.remove(currentMovie)
        movies.add(movie)
        return movie
    }

    override fun deleteMovie(id: Int) {
        val currentMoive = movies.firstOrNull { it.id == id }
            ?: throw NoSuchElementException("Cound not find a bank with account number $id")
        movies.remove(currentMoive)
    }
}