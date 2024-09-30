package com.hibob.academy.employeeFeedback.utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.hibob.academy.employeeFeedback.dao.filter.Filter
import com.hibob.academy.employeeFeedback.model.GenericFilterRequest

class GenericFilterRequestDeserializer : JsonDeserializer<GenericFilterRequest>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): GenericFilterRequest {
        val node: JsonNode = parser.codec.readTree(parser)
        val filtersNode = node.get("filters")
        if (filtersNode == null || !filtersNode.isObject) {
            throw JsonMappingException("Expected 'filters' field to be an object")
        }

        val filterList = filtersNode.fields().asSequence().mapNotNull { entry ->
            Filter.fromJson(entry.key, entry.value.asText())
        }.toList()

        return GenericFilterRequest(filterList)
    }
}
