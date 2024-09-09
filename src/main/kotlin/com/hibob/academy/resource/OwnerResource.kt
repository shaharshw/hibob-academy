package com.hibob.academy.resource

import com.hibob.academy.entity.Owner
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status
import org.springframework.stereotype.Controller

@Controller
@Path("/api/owner")
@Produces(MediaType.APPLICATION_JSON)
class OwnerResource {

    @GET
    fun getALLOwners() : Response {
        return Response.ok(listOf(
            Owner(1, "Shahar", 1, 1, null,null),
            Owner(2, "Alex", 1, 2, null,null )
        ))
            .build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun createOwner(owner: Owner): Response {

        owner.name = owner.name ?: run {
            if (owner.firstName.isNullOrEmpty() || owner.lastName.isNullOrEmpty()) {
                return Response.status(Status.BAD_REQUEST).entity("Name is required").build()
            } else {
                "${owner.firstName} ${owner.lastName}"
            }
        }

        owner.name?.let { fullName ->
            val nameParts = fullName.split(" ")
            owner.firstName = nameParts[0]
            owner.lastName = nameParts.getOrNull(1)
        }

        return Response.status(Status.CREATED).entity(owner).build()
    }

    @PUT
    @Path("/{ownerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    fun updateOwner(@PathParam("ownerId") ownerId: Long, owner: Owner) : Response {
        return Response.ok(owner).build()
    }

    @DELETE
    @Path("/{ownerId}")
    fun deletePet(@PathParam("ownerId") ownerId: Long) : Response {
        return Response.ok().build()
    }
}