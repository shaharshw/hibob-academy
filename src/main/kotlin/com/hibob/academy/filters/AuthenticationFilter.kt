package com.hibob.academy.filters

import com.hibob.academy.service.SessionService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Component

@Component
class AuthenticationFilter : ContainerRequestFilter {

    override fun filter(requestContext: ContainerRequestContext) {

        val path = requestContext.uriInfo.path

        if (path == "api/auth/login") return

        val authorizationHeader = requestContext.headers["Authorization"]?.firstOrNull()
        val token = authorizationHeader?.removePrefix("Bearer ")?.trim()
        val claims = validateToken(token)

        if (claims == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build())
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