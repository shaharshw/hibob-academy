package com.hibob.academy.employeeFeedback.dao.filter

import com.hibob.academy.employeeFeedback.dao.table.EmployeeTable
import com.hibob.academy.employeeFeedback.dao.table.FeedbackTable
import com.hibob.academy.employeeFeedback.model.Department
import com.hibob.academy.employeeFeedback.model.FeedbackStatus
import com.hibob.academy.employeeFeedback.model.GenericFilterRequest
import java.time.LocalDate
import org.jooq.Condition

object FilterFactory {

    private val filterMap: Map<String, (Any?) -> Filter<*>?> = mapOf(
        "date" to { value -> value?.let { Filter.DateFilter(LocalDate.parse(it as String)) } },
        "status" to { value -> value?.let { Filter.StatusFilter(FeedbackStatus.valueOf(it as String)) } },
        "isAnonymous" to { value -> value?.let { Filter.IsAnonymousFilter(it as Boolean) } },
        "department" to { value -> value?.let { Filter.DepartmentFilter(Department.valueOf(it as String)) } },
        // Add more filter mappings here as needed
    )

    private fun createFilter(key: String, value: Any?): Filter<*>? {
        return filterMap[key]?.invoke(value)
    }

    fun convertToGenericFilterRequest(filters: Map<String, Any>): GenericFilterRequest {
        val filterList = filters.mapNotNull { (key, value) ->
            createFilter(key, value)
        }

        return GenericFilterRequest(filterList)
    }
}
