package com.hibob.academy.employeeFeedback.dao.filter

object FilterFactory {
    private var registry: Map<String, (String?) -> Filter<*>?> = emptyMap()

    fun register(key: String, creator: (String?) -> Filter<*>?) {
        registry = registry + (key to creator)
    }

    fun createFilter(key: String, value: String?): Filter<*>? {
        return registry[key]?.invoke(value)
    }
}