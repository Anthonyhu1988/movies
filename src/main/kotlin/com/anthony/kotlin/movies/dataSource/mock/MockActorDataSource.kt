package com.anthony.kotlin.movies.dataSource.mock

import com.anthony.kotlin.movies.dataSource.ActorDataSource
import com.anthony.kotlin.movies.model.Actor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.lang.IllegalArgumentException

@Repository
class MockActorDataSource : ActorDataSource {

    @Autowired
    lateinit var mockMovieDataSource: MockMovieDataSource

    val actors = mutableListOf(
        Actor(1, "Robert Downey Jr."),
        Actor(2, "Gwyneth Paltrow"),
        Actor(3, "Jeff Bridges"),
        Actor(4, "Mark Wahlberg"),
        Actor(5, "Taylor Kitsch"),
        Actor(6, "Ben Foster"),
        Actor(7, "Robert Downey Jr."),
        Actor(8, "Jude Law"),
        Actor(9, "Mark Strong"),
        Actor(20, "Tobey Maguire")
    )


    override fun retrieveActors(): Collection<Actor> = actors

    override fun retrieveActor(id: Int): Actor = actors.firstOrNull { it.id == id }
        ?: throw NoSuchElementException("Could not find a Actor with ID number of $id")

    override fun createActor(actor: Actor): Actor {
        if (actor.name.isEmpty()) {
            throw IllegalArgumentException("Actor's name can not be empty")
        }

        if (actor.id < 0) {
            throw IllegalArgumentException("Actor's id must be a number small than 0")
        }

        if (actors.any { it.name == actor.name}) {
            throw IllegalArgumentException("Actor with name of ${actor.name} already exist ")
        }

        if (actors.any { it.id == actor.id}) {
            throw IllegalArgumentException("Actor with id of ${actor.id} already exist ")
        }

        actors.add(actor)
        return actor
    }

    override fun updateActor(actor: Actor): Actor {
        val currentActor = actors.firstOrNull { it.id == actor.id}
            ?: throw NoSuchElementException("Could not find a actor with id of ${actor.id}")
        actors.remove(currentActor)
        actors.add(actor)
        return actor
    }

    override fun deleteActor(id: Int) {
        val currentActor = actors.firstOrNull { it.id == id}
            ?: throw NoSuchElementException("Could not find a actor with id of $id")

        if (mockMovieDataSource.movies.any { movie -> movie.actors.any { it.name == currentActor.name }}) {
            throw IllegalArgumentException("Actor with id of ${currentActor.id} can not be deleted due to the act ${currentActor.name} in a movie")
        }

        actors.remove(currentActor)
    }
}