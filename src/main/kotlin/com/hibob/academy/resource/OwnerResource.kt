package com.hibob.academy.resource

import com.hibob.academy.entity.Owner
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status
import org.springframework.stereotype.Controller
import java.util.*

@Controller
@Path("/api/owner")
@Produces(MediaType.APPLICATION_JSON)
class OwnerResource {

    @GET
    fun getALLOwners() : Response {
        return Response.ok(listOf(
            Owner(1L, "Shahar", 1L, "1", null, null),
            Owner(2L, "Alex", 2L, "2", null, null)
        ))
            .build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun createOwner(owner: Owner): Response {

        owner.name = owner.name ?: run {
            if (owner.firstName.isNullOrEmpty() || owner.lastName.isNullOrEmpty()) {
                throw BadRequestException("Name is required")
            } else {
                "${owner.firstName} ${owner.lastName}"
            }
        }

        owner.name?.let { fullName ->
            val nameParts = fullName.trim().split("\\s+".toRegex())
            owner.firstName = nameParts[0]
            owner.lastName = nameParts.drop(1).joinToString(" ").takeIf { it.isNotEmpty() }
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