package com.anthony.kotlin.movies.service

import com.anthony.kotlin.movies.dataSource.ActorDataSource
import com.anthony.kotlin.movies.model.Actor
import org.springframework.stereotype.Service

@Service
class ActorService (private val dataSource: ActorDataSource) {

    fun getActors(): Collection<Actor> = dataSource.retrieveActors()

    fun getActors(id: Int): Actor = dataSource.retrieveActor(id)

    fun addActor(actor: Actor): Actor = dataSource.createActor(actor)

    fun updateActor(actor: Actor): Actor = dataSource.updateActor(actor)

    fun deleteActor(id: Int): Unit = dataSource.deleteActor(id)
}