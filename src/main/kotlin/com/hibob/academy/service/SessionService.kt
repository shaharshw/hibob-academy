package com.hibob.academy.service

import com.hibob.academy.employeeFeedback.dao.EmployeeDao
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import com.hibob.academy.employeeFeedback.model.Role
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

@Component
class SessionService(
    private val employeeDao: EmployeeDao
) {

    companion object {

        const val SECRET_KEY: String = "secrethashkeyshahar12323445665713124jadnasdkllFSDFNSD89SLJNLASJNLD"
    }

    fun createJwtToken(user : LoggedInUser): String {

        val expirationDate : Date = Date.from(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC))

        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .claim("UserId", user.id)
            .claim("CompanyId", user.companyId)
            .setIssuedAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact()
    }

    fun getCurrentUserRoleFromToken(token: String): Role {

        val userId = extractUserId(token)
        val companyId = extractCompanyId(token)
        val loggedInUser = LoggedInUser(userId, companyId)

        return employeeDao.getRoleFromLoggedInUser(loggedInUser)
    }

    private fun extractUserId(token: String): Long {
        return (Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .body["UserId"] as Int).toLong()
    }

    private fun extractCompanyId(token: String): Long {
        return (Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .body["CompanyId"] as Int).toLong()
    }
}