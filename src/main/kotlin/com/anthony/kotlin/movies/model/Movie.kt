package com.anthony.kotlin.movies.model

data class Movie(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val actors : MutableList<Actor>
)
