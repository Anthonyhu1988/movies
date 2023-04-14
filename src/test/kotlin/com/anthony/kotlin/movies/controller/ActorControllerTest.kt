package com.anthony.kotlin.movies.controller

import com.anthony.kotlin.movies.model.Actor
import com.fasterxml.jackson.databind.ObjectMapper
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
class ActorControllerTest @Autowired constructor(
    var mockMvc: MockMvc,
    var objectMapper: ObjectMapper
){

    val bastURL = "/api/actors"

    @Nested
    @DisplayName("Get /api/actors")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetActors {
        @Test
        fun `should return all actors` () {
            //when/then
            mockMvc.get(bastURL)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].name") {value("Robert Downey Jr.")}
                }
        }
    }

    @Nested
    @DisplayName("Get /api/actors/{id}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetActor {
        @Test
        fun `should return the actor with given id` () {
            //given
            val id = 1

            //when/THEN
            mockMvc.get("$bastURL/$id")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.name") {value("Robert Downey Jr.")}
                }
        }

        @Test
        fun `should return NOT FOUND if the Actor id does not exist` () {
            //given
            val id = 14

            //when/then
            mockMvc.get("$bastURL/$id")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("POST /api/actors")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class  PostNewActor{

        @Test
        fun `should add the new actor` () {
            //given
            val newActor = Actor(10, "Gal Gadot")

            //when
            val performPost = mockMvc.post(bastURL){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newActor)
            }

            //then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(newActor))
                    }
                }
            mockMvc.get("$bastURL/${newActor.id}")
                .andExpect { content { json(objectMapper.writeValueAsString(newActor)) } }
        }

        @Test
        fun `should return BAD REQUEST if actor with given name already exists` () {
            //given
            val invalidActor = Actor(10, "Gwyneth Paltrow")

            //when
            val performPost = mockMvc.post(bastURL){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidActor)
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
    @DisplayName("PATCH /api/actors")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchExistingActor {
        @Test
        fun `should update an existing actor` () {
            //given
            val updatedActor = Actor(1, "Tony Stark")

            //when
            val performPatch = mockMvc.patch(bastURL){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedActor)
            }

            //then
            performPatch
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updatedActor))
                    }
                }
            mockMvc.get("$bastURL/${updatedActor.id}")
                .andExpect { content { json(objectMapper.writeValueAsString(updatedActor)) } }
        }

        @Test
        fun `should return BAD REQUEST is no actor with given id exist` () {
            //given
            val invalidActor = Actor(21, "Tom Hanks")

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
    @DisplayName("Delete /api/actors/{id}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteExistingActor {
        @Test
        @DirtiesContext
        fun `should delete the actor with the given id if actor does not appear in any movie` () {
            //given
            val id = 20

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
        fun `should return NOT FOUND if no actor with given id exists ` () {
            //given
            val invalidId = 14

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