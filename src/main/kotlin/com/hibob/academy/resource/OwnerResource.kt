package com.hibob.academy.resource

import com.hibob.academy.entity.Owner
import com.hibob.academy.service.OwnerService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status
import org.springframework.stereotype.Controller
import java.util.*

@Controller
@Path("/api/owner")
@Produces(MediaType.APPLICATION_JSON)
class OwnerResource(
    private val ownerService: OwnerService
) {

    @GET
    @Path("/all/{companyId}")
    fun getALLOwners(@PathParam("companyId") companyId : Long) : Response {
        return Response.ok(ownerService.getAllOwnersByCompanyId(companyId)).build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun createOwner(owner: Owner): Response {

        val owner = ownerService.createOwner(owner)
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