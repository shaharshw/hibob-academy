package com.hibob.academy.resource

import com.hibob.academy.entity.Owner
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller

@Controller
@Path("/api/owner")
@Produces(MediaType.APPLICATION_JSON)
class OwnerResource {

    @GET
    fun getALLOwners() : Response {
        return Response.ok(listOf(
            Owner(1, "Shahar", 1, 1),
            Owner(2, "Alex", 1, 2)
        ))
            .build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun createOwner(owner: Owner) : Response {
        return Response.ok(owner).build()
    }

    @PUT
    @Path("/{ownerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    fun updateOwner(owner: Owner) : Response {
        return Response.ok(owner).build()
    }

    @DELETE
    @Path("/{ownerId}")
    fun deletePet(@PathParam("ownerId") id: Long) : Response {
        return Response.ok().build()
    }
}