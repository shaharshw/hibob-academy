package com.hibob.academy.service

import com.hibob.academy.dao.PetDao
import org.springframework.stereotype.Service

@Service
class PetService(
    private val petDao: PetDao
) {
}