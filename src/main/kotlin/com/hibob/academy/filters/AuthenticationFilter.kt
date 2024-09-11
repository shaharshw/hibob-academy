package com.hibob.academy.filters

import com.hibob.academy.service.SessionService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.Cookie
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider
import org.springframework.stereotype.Component




@Component
@Provider
class AuthenticationFilter : ContainerRequestFilter {

    companion object {

        const val PATH_TO_SKIP = "api/auth/login"

        const val AUTH_COOKIE_NAME = "Authorization"
    }

    override fun filter(requestContext: ContainerRequestContext) {

        val path = requestContext.uriInfo.path

        if (path == PATH_TO_SKIP) return

        val cookies: Map<String, Cookie> = requestContext.cookies
        val token = cookies[AUTH_COOKIE_NAME]?.value?.trim()
        val claims = validateToken(token, requestContext)
    }

     private fun validateToken(token: String?, requestContext: ContainerRequestContext) {

         token?.let {
             try {
                 Jwts.parser()
                     .setSigningKey(SessionService.SECRET_KEY)
             } catch (e: Exception) {
                 requestContext.abortWith(
                     Response.status(Response.Status.UNAUTHORIZED)
                         .entity("User can't access to the resource")
                         .build()
                 )
             }
         }
     }
}