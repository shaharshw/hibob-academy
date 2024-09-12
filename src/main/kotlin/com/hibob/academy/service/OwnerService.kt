package com.hibob.academy.service

import com.hibob.academy.dao.OwnerDao
import org.springframework.stereotype.Service

@Service
class OwnerService(
    private val ownerDao: OwnerDao
) {
}