package com.hibob.academy.employeeFeedback.dao.filter

import com.hibob.academy.employeeFeedback.dao.table.EmployeeTable
import com.hibob.academy.employeeFeedback.dao.table.FeedbackTable
import com.hibob.academy.employeeFeedback.model.Department
import com.hibob.academy.employeeFeedback.model.FeedbackStatus
import org.jooq.Condition
import org.jooq.impl.DSL.upper
import java.sql.Date
import java.time.LocalDate

sealed class Filter<T> {
    abstract fun apply(table: T): Condition?

    data class DepartmentFilter(private val department: Department?) : Filter<EmployeeTable>() {
        override fun apply(table: EmployeeTable): Condition? {
            return department?.name?.let {
                upper(table.department).eq(it.uppercase())
            }
        }
    }

    data class DateFilter(private val date: LocalDate?) : Filter<FeedbackTable>() {
        override fun apply(table: FeedbackTable): Condition? {
            return date?.let { Date.valueOf(it) }?.let { table.createdAt.gt(it) }
        }
    }

    data class StatusFilter(private val status: FeedbackStatus?) : Filter<FeedbackTable>() {
        override fun apply(table: FeedbackTable): Condition? {
            return status?.name?.let {
                upper(table.status).eq(it.uppercase())
            }
        }
    }

    data class IsAnonymousFilter(private val isAnonymous: Boolean?) : Filter<FeedbackTable>() {
        override fun apply(table: FeedbackTable): Condition? {
            return isAnonymous?.let { table.isAnonymous.eq(it) }
        }
    }
}