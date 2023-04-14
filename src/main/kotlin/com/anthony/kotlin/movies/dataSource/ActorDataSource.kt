package com.anthony.kotlin.movies.dataSource

import com.anthony.kotlin.movies.model.Actor

interface ActorDataSource {

    fun retrieveActors(): Collection<Actor>

    fun retrieveActor(id: Int): Actor

    fun createActor(actor: Actor): Actor

    fun updateActor(actor: Actor): Actor

    fun deleteActor(id: Int)
}