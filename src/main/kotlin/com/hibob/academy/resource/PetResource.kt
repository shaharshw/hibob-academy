package com.hibob.academy.resource

import com.hibob.academy.entity.AdoptPetsRequest
import com.hibob.academy.entity.CreatePetRequest
import com.hibob.academy.entity.Pet
import com.hibob.academy.service.PetService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@Controller
@Path("/api/pet")
@Produces(MediaType.APPLICATION_JSON)
class PetResource(
    private val petService: PetService
) {

    @GET
    @Path("/{companyId}")
    fun getALLPetsByCompanyId(@PathParam("companyId") companyId : Long) : Response {

        val pets = petService.getAllPets(companyId)
        return Response.ok(pets).build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun createPet(@RequestBody createPetRequest: CreatePetRequest): Response {

        val petId = petService.createPet(createPetRequest)
        return Response.status(Response.Status.CREATED).entity(petId).build()
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
    @Path("/owner/{ownerId}")
    fun getAllPetsByOwner(@PathParam("ownerId") ownerId: Long) : Response {
        return Response.ok(
            petService.getAllPetsByOwnerId(ownerId)
        )
            .build()
    }

    @POST
    @Path("/adopt/many")
    fun adoptPets(@RequestBody adoptPetsRequest: AdoptPetsRequest) : Response {

        val results = petService.adoptPets(adoptPetsRequest)
        return Response.ok(results).build()
    }

    @GET
    @Path("/types/count")
    fun getCountPetsByType() : Response {
        return Response.ok(
            petService.getCountPetsByType()
        )
            .build()
    }

    @POST
    @Path("/many")
    fun createPets(@RequestBody pets: List<CreatePetRequest>) : Response {
        val petIds = petService.createPets(pets)
        return Response.ok(petIds).build()
    }

}