package com.hibob.academy.service

import com.hibob.academy.resource.LoginUser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

@Component
class SessionService {

    companion object {

        const val SECRET_KEY: String = "secrethashkeyshahar12323445665713124jadnasdkllFSDFNSD89SLJNLASJNLD"
    }

    fun createJwtToken(user : LoginUser): String {

        val now : Date = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC))
        val expirationDate : Date = Date.from(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC))

        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .claim("Email", user.email)
            .claim("UserName", user.userName)
            .claim("IsAdmin", user.isAdmin)
            .setIssuedAt(now)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact()
    }

}