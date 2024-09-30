package com.hibob.academy.employeeFeedback.dao.filter

import com.hibob.academy.employeeFeedback.dao.table.EmployeeTable
import com.hibob.academy.employeeFeedback.dao.table.FeedbackTable
import com.hibob.academy.employeeFeedback.model.DateRange
import com.hibob.academy.employeeFeedback.model.Department
import com.hibob.academy.employeeFeedback.model.FeedbackStatus
import org.jooq.Condition
import org.json.JSONObject
import java.sql.Date
import java.time.LocalDate
import kotlin.reflect.full.createInstance

const val DATE = "date"
const val STATUS = "status"
const val IS_ANONYMOUS = "isAnonymous"
const val DEPARTMENT = "department"

interface FilterType {
    fun register()
}

interface FeedbackFilter {
    fun apply(table: FeedbackTable): Condition?
}

interface EmployeeFilter {
    fun apply(table: EmployeeTable): Condition?
}

sealed class Filter<T> {
    abstract fun apply(table: T): Condition?

    companion object {
        init {
            val filterClasses = Filter::class.sealedSubclasses
            filterClasses.forEach { subclass ->
                val instance = subclass.createInstance()
                (instance as? FilterType)?.register()
            }
        }

        fun fromJson(key: String, value: String?): Filter<*>? {
            return FilterFactory.createFilter(key, value)
        }
    }

    data class DepartmentFilter(private val department: Department? = null) : Filter<EmployeeTable>(), EmployeeFilter, FilterType {
        override fun apply(table: EmployeeTable): Condition? {
            return department?.name?.let {
                table.department.eq(it)
            }
        }

        companion object {
            fun fromJson(value: String?): DepartmentFilter? {
                return value?.let { DepartmentFilter(Department.valueOf(it)) }
            }
        }

        override fun register() {
            FilterFactory.register(DEPARTMENT, DepartmentFilter.Companion::fromJson)
        }
    }

    data class DateFilter(private val date: LocalDate? = null) : Filter<FeedbackTable>(), FeedbackFilter, FilterType {
        override fun apply(table: FeedbackTable): Condition? {
            return date?.let { Date.valueOf(it) }?.let { table.createdAt.gt(it) }
        }

        companion object {
            fun fromJson(value: String?): DateFilter? {
                return value?.let { DateFilter(LocalDate.parse(it)) }
            }
        }

        override fun register() {
            FilterFactory.register(DATE, DateFilter.Companion::fromJson)
        }
    }

    data class StatusFilter(private val status: FeedbackStatus? = null) : Filter<FeedbackTable>(), FeedbackFilter, FilterType {
        override fun apply(table: FeedbackTable): Condition? {
            return status?.name?.let {
                table.status.eq(it)
            }
        }

        companion object {
            fun fromJson(value: String?): StatusFilter? {
                return value?.let { StatusFilter(FeedbackStatus.valueOf(it)) }
            }
        }

        override fun register() {
            FilterFactory.register(STATUS, StatusFilter.Companion::fromJson)
        }
    }

    data class IsAnonymousFilter(private val isAnonymous: Boolean? = null) : Filter<FeedbackTable>(), FeedbackFilter, FilterType {
        override fun apply(table: FeedbackTable): Condition? {
            return isAnonymous?.let { table.isAnonymous.eq(it) }
        }

        companion object {
            fun fromJson(value: String?): IsAnonymousFilter? {
                return value?.let { IsAnonymousFilter(it.toBoolean()) }
            }
        }

        override fun register() {
            FilterFactory.register(IS_ANONYMOUS, IsAnonymousFilter.Companion::fromJson)
        }
    }
}