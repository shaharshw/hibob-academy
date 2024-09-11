
package com.hibob.academy.resource

import com.hibob.academy.entity.Pet
import com.hibob.academy.entity.PetType
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import java.time.LocalDate

@Controller
@Path("/api/pet")
@Produces(MediaType.APPLICATION_JSON)
class PetResource {

    @GET
    fun getALLPets() : Response{
        val pets = listOf(
            Pet(1, "Bobby", PetType.DOG, LocalDate.parse("2021-01-01"), 1),
            Pet(2, "Kitty", PetType.CAT, LocalDate.parse("2021-01-01"), 1)
        )
        return Response.ok(pets).build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun createPet(@RequestBody pet: Pet): Response {
        return Response.ok(pet).build()
    }

    @PUT
    @Path("/{petId}")
    fun updatePet(@PathParam("petId") petId : Long, @RequestBody pet: Pet) : Response {
        return Response.ok(pet).build()
    }

    @DELETE
    @Path("/{petId}")
    fun deletePet(@PathParam("petId") petId: Long) : Response {
        return Response.ok().build()
    }
}
