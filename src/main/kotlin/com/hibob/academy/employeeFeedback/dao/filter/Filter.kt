package com.hibob.academy.employeeFeedback.dao.filter

import com.hibob.academy.employeeFeedback.dao.table.EmployeeTable
import com.hibob.academy.employeeFeedback.dao.table.FeedbackTable
import com.hibob.academy.employeeFeedback.model.Department
import com.hibob.academy.employeeFeedback.model.FeedbackStatus
import org.jooq.Condition
import java.sql.Date
import java.time.LocalDate

interface FeedbackFilter {
    fun apply(table: FeedbackTable): Condition?
}

interface EmployeeFilter {
    fun apply(table: EmployeeTable): Condition?
}

sealed class Filter<T> {
    abstract fun apply(table: T): Condition?

    data class DepartmentFilter(private val department: Department?) : Filter<EmployeeTable>(), EmployeeFilter {
        override fun apply(table: EmployeeTable): Condition? {
            return department?.name?.let {
                table.department.eq(it)
            }
        }
    }

    data class DateFilter(private val date: LocalDate?) : Filter<FeedbackTable>(), FeedbackFilter {
        override fun apply(table: FeedbackTable): Condition? {
            return date?.let { Date.valueOf(it) }?.let { table.createdAt.gt(it) }
        }
    }

    data class StatusFilter(private val status: FeedbackStatus?) : Filter<FeedbackTable>(), FeedbackFilter {
        override fun apply(table: FeedbackTable): Condition? {
            return status?.name?.let {
                table.status.eq(it)
            }
        }
    }

    data class IsAnonymousFilter(private val isAnonymous: Boolean?) : Filter<FeedbackTable>(), FeedbackFilter {
        override fun apply(table: FeedbackTable): Condition? {
            return isAnonymous?.let { table.isAnonymous.eq(it) }
        }
    }
}