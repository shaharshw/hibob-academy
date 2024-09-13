
package com.hibob.academy.resource

import com.hibob.academy.entity.Pet
import com.hibob.academy.entity.PetType
import com.hibob.academy.service.PetService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import java.time.LocalDate
import java.util.*

@Controller
@Path("/api/pet")
@Produces(MediaType.APPLICATION_JSON)
class PetResource(
    private val petService: PetService
) {

    @GET
    fun getALLPets() : Response{
        val pets = listOf(
            Pet(1L, "Bobby", PetType.DOG, LocalDate.parse("2021-01-01"), 1, 1L),
            Pet(2L, "Kitty", PetType.CAT, LocalDate.parse("2021-01-01"), 1, 2L)
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

    @PUT
    @Path("/{petId}/owner/{ownerId}")
    fun assignOwnerToPet(@PathParam("petId") petId: Long, @PathParam("ownerId") ownerId: Long) : Response {

        val updatePet = petService.assignOwnerToPet(petId, ownerId)
        return Response.ok(updatePet).build()
    }

    @GET
    @Path("/{petId}/owner")
    fun getOwnerByPetId(@PathParam("petId") petId: Long) : Response {

        val owner = petService.getOwnerByPetId(petId)
        return Response.ok(owner).build()
    }

    @GET
    @Path("/all/{ownerId}")
    fun getAllPetsByOwner(@PathParam("ownerId") ownerId: Long) : Response {
        return Response.ok(
            petService.getAllPetsByOwnerId(ownerId)
        )
            .build()
    }

    @GET
    @Path("/all/types")
    fun getCountPetsByType() : Response {
        return Response.ok(
            petService.getCountPetsByType()
        )
            .build()
    }
}
