package com.anthony.kotlin.movies.controller

import com.anthony.kotlin.movies.model.Actor
import com.anthony.kotlin.movies.model.Movie
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerTest @Autowired constructor(
    var mockMvc: MockMvc,
    var objectMapper: ObjectMapper
){
    val bastURL = "/api/movies"

    @Nested
    @DisplayName("Get /api/movies")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetMovies {
        @Test
        fun `should return all movies` () {
            //when/then
            mockMvc.get(bastURL)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].title") {value("Iron Man")}
                    jsonPath("$[0]..actors.length()") {value(3)}
                    jsonPath("$[0]..actors.length()") {Matchers.greaterThanOrEqualTo(1)}
                    jsonPath("$[0]..actors[1].name") {value("Gwyneth Paltrow")}
                }
        }
    }

    @Nested
    @DisplayName("Get /api/movies/{id}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetMovie {
        @Test
        fun `should return the movie with given id` () {
            //given
            val id = 1

            //when/THEN
            mockMvc.get("$bastURL/$id")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.title") {value("Iron Man")}
                    jsonPath("$..actors.length()") {value(3)}
                    jsonPath("$..actors.length()") {Matchers.greaterThanOrEqualTo(1)}
                    jsonPath("$..actors[1].name") {value("Gwyneth Paltrow")}
                }
        }

        @Test
        fun `should return NOT FOUND if the movie id does not exist` () {
            //given
            val id = 4

            //when/then
            mockMvc.get("$bastURL/$id")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("POST /api/movies")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class  PostNewMovie{

        @Test
        fun `should add the new movie` () {
            //given
            val newActor = Actor(10, "Gal Gadot")
            val newActors = mutableListOf(newActor)
            val newMovie = Movie(6, "Wonder Woman 1984", "2020-12-25", newActors)

            //when
            val performPost = mockMvc.post(bastURL){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newMovie)
            }

            //then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(newMovie))
                    }
                }
            mockMvc.get("$bastURL/${newMovie.id}")
                .andExpect { content { json(objectMapper.writeValueAsString(newMovie)) } }
        }

        @Test
        fun `should return BAD REQUEST if movie with given title and release date combines are already exists` () {
            //given
            val newActor = Actor(10, "Gal Gadot")
            val newActors = mutableListOf(newActor)
            val invalidMoive = Movie(4, "Sherlock Holmes", "2009-12-25", newActors)

            //when
            val performPost = mockMvc.post(bastURL){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidMoive)
            }

            //then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }

        @Test
        fun `should return BAD REQUEST if movie with given actor list with no actors` () {
            //given
            val newActor = Actor(10, "Gal Gadot")
            val newActors = mutableListOf(newActor)
            val invalidMoive = Movie(4, "Sherlock Holmes", "2009-12-25", newActors)

            //when
            val performPost = mockMvc.post(bastURL){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidMoive)
            }

            //then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }

        @Test
        fun `should return BAD REQUEST if movie with given actor list has duplications` () {
            //given
            val newActor1 = Actor(10, "Gal Gadot")
            val newActor2 = Actor(11, "Gal Gadot")
            val newActor3 = Actor(13, "aa")
            val newActor4 = Actor(14, "bb")
            val newActors = mutableListOf(newActor1, newActor2, newActor3, newActor4)
            val invalidMoive = Movie(9, "abc", "2009-10-25", newActors)

            //when
            val performPost = mockMvc.post(bastURL){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidMoive)
            }

            //then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    @DisplayName("PATCH /api/movies")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchExistingMovie {
        @Test
        fun `should update an existing move` () {
            //given
            val actors = mutableListOf(
                Actor(1, "Tony Stark"),
                Actor(2, "Gwyneth Paltrow"),
                Actor(3, "Jeff Bridges")
            )

            val updatedMovie = Movie(1, "Iron Man", "2008-05-02", actors)

            //when
            val performPatch = mockMvc.patch(bastURL){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedMovie)
            }

            //then
            performPatch
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updatedMovie))
                    }
                }
            mockMvc.get("$bastURL/${updatedMovie.id}")
                .andExpect { content { json(objectMapper.writeValueAsString(updatedMovie)) } }
        }

        @Test
        fun `should return BAD REQUEST is no movie with given id exist` () {
            //given
            val actors = mutableListOf(
                Actor(1, "Tony Stark"),
                Actor(2, "Gwyneth Paltrow"),
                Actor(3, "Jeff Bridges")
            )

            val invalidActor = Movie(10, "Iron Man", "2008-05-02", actors)

            //when
            val performPatchRequest = mockMvc.patch(bastURL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidActor)
            }

            //then
            performPatchRequest
                .andDo { print() }
                .andExpect {
                    status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("Delete /api/movies/{id}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteExistingMovie {
        @Test
        @DirtiesContext
        fun `should delete the movie with the given id` () {
            //given
            val id = 1

            //when/then
            mockMvc.delete("$bastURL/${id}")
                .andDo{print()}
                .andExpect {
                    status { isNoContent() }
                }
            mockMvc.get("$bastURL/${id}")
                .andExpect { status { isNotFound() } }
        }

        @Test
        fun `should return NOT FOUND if no movie with given id exists ` () {
            //given
            val invalidId = 4

            //when
            mockMvc.delete("$bastURL/$invalidId")
                .andDo{print()}
                .andExpect {
                    status { isNotFound() }
                }

            //then

        }
    }
}