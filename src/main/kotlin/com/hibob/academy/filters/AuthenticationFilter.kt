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

    override fun filter(requestContext: ContainerRequestContext) {

        val path = requestContext.uriInfo.path

        if (path == "api/auth/login") return

        val cookies: Map<String, Cookie> = requestContext.cookies
        val token = cookies["Authorization"]?.value?.trim()
        val claims = validateToken(token)

        if (claims == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity("User can't access to the resource")
                .build())
        }
    }

    private fun validateToken(token: String?): Jws<Claims>? {

        return token?.let {
            try {
                Jwts.parser()
                    .setSigningKey(SessionService.SECRET_KEY)
                    .parseClaimsJws(it)
            } catch (e: Exception) {
                null
            }
        }
    }
}