package com.anthony.kotlin.movies.controller

import com.anthony.kotlin.movies.model.Actor
import com.anthony.kotlin.movies.service.ActorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/actors")
class ActorController (private val service: ActorService) {

    @ExceptionHandler(NoSuchElementException ::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException ::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @GetMapping
    fun getActors(): Collection<Actor> = service.getActors()

    @GetMapping("/{id}")
    fun getActors(@PathVariable id: Int) = service.getActors(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addActor(@RequestBody actor : Actor): Actor = service.addActor(actor)

    @PatchMapping
    fun updateActor(@RequestBody actor : Actor): Actor = service.updateActor(actor)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteActor(@PathVariable id: Int) = service.deleteActor(id)
}