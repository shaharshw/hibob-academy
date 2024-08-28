package com.hibob.academy.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.hibob.academy.utils.KotlinJooq.jacksonConcreteType
import org.jooq.*
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl
import org.jooq.types.YearToSecond
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

object KotlinJooq {
    private val objectMapper = ObjectMapper().registerKotlinModule()

    val instantType: DataType<Instant> = SQLDataType.TIMESTAMP.asConvertedDataType(object : Converter<Timestamp, Instant> {
        override fun from(databaseObject: Timestamp?): Instant? = databaseObject?.toInstant()
        override fun to(userObject: Instant?): Timestamp? = userObject?.let { Timestamp.from(it) }
        override fun fromType(): Class<Timestamp> = Timestamp::class.java
        override fun toType(): Class<Instant> = Instant::class.java
    })

    private val jacksonNodeConverter: Converter<JSONB, JsonNode> = object : JsonbConverter<JsonNode>() {
        override fun toType(): Class<JsonNode> = JsonNode::class.java

        override fun from(databaseObject: JSONB?): JsonNode? =
            databaseObject?.let {
                objectMapper.readTree(it.data())
            }
    }

    private val jacksonObjectConverter: Converter<JSONB, ObjectNode> = object : JsonbConverter<ObjectNode>() {
        override fun toType(): Class<ObjectNode> = ObjectNode::class.java

        override fun from(databaseObject: JSONB?): ObjectNode? =
            databaseObject?.let {
                objectMapper.readValue(it.data(), ObjectNode::class.java)
            }
    }

    fun <T> jacksonConcreteType(clazz: Class<T>): DataType<T> =
        SQLDataType.JSONB.asConvertedDataType(object : Converter<JSONB, T> {

            override fun toType(): Class<T> = clazz

            override fun fromType(): Class<JSONB> = JSONB::class.java

            override fun from(databaseObject: JSONB?): T? =
                databaseObject?.let {
                    objectMapper.readValue(it.data(), clazz)
                }

            override fun to(userObject: T?): JSONB? =
                userObject?.let {
                    JSONB.valueOf(objectMapper.writeValueAsString(it))
                }
        })

    val jacksonNodeType: DataType<JsonNode> = SQLDataType.JSONB.asConvertedDataType(jacksonNodeConverter)
    val jacksonObjectType: DataType<ObjectNode> = SQLDataType.JSONB.asConvertedDataType(jacksonObjectConverter)

    fun jsonText(field: Field<*>, name: String): Field<String> {
        return DSL.field("{0}->>{1}", String::class.java, field, DSL.inline(name))
    }

    fun daysToInterval(days: Field<Int>): Field<YearToSecond> {
        val zero = DSL.value(0)
        return DSL.function("make_interval", SQLDataType.INTERVAL, zero, zero, zero, days)
    }

    fun monthsToInterval(months: Field<Int>): Field<YearToSecond> {
        val zero = DSL.value(0)
        return DSL.function("make_interval", SQLDataType.INTERVAL, zero, months)
    }

    fun jsonbSet(target: Field<ObjectNode>, path: List<String>,
                 newValue: Field<ObjectNode>, createMissing: Boolean = true): Field<ObjectNode> =
        DSL.function("jsonb_set", ObjectNode::class.java, target,
            DSL.`val`(path, Array<String>::class.java), newValue, DSL.`val`(createMissing)
        )

    fun toJsonb(source: Field<*>): Field<ObjectNode> {
        return if(source.name == "null") {
            DSL.field("'null'::jsonb", ObjectNode::class.java)
        } else {
            DSL.function("to_jsonb", ObjectNode::class.java, source)
        }
    }

    inline fun <reified E : Enum<E>> createEnumConverter(): Converter<String, E> = object : Converter<String, E> {
        private fun fromDBValue(dbValue: String?): E =
            enumValues<E>().find { it.name.equals(dbValue, ignoreCase = true) }
                ?: throw RuntimeException("'$dbValue' is not a value for ${E::class.simpleName} ")

        override fun from(databaseObject: String?): E = fromDBValue(databaseObject)
        override fun to(userObject: E?): String? = userObject?.name?.lowercase()
        override fun fromType(): Class<String> = String::class.java
        override fun toType(): Class<E> = E::class.java
    }
}

abstract class JsonbConverter<T : JsonNode> : Converter<JSONB, T> {
    abstract override fun toType(): Class<T>

    override fun fromType(): Class<JSONB> = JSONB::class.java

    abstract override fun from(databaseObject: JSONB?): T?

    override fun to(userObject: T?): JSONB? =
        userObject?.let {
            JSONB.valueOf(it.toString())
        }
}


typealias TableField<T> = org.jooq.TableField<Record, T>

abstract class JooqTable(tableName: String, aliased: JooqTable? = null) : TableImpl<Record>(DSL.name(tableName), null, aliased) {

    protected fun createNumericField(fieldName: String): TableField<BigDecimal> =
        createField(DSL.name(fieldName), SQLDataType.NUMERIC)

    protected fun createBigIntField(fieldName: String): TableField<Long> =
        createField(DSL.name(fieldName), SQLDataType.BIGINT)

    protected fun createIntField(fieldName: String): TableField<Int> =
        createField(DSL.name(fieldName), SQLDataType.INTEGER)

    protected fun createVarcharField(fieldName: String): TableField<String> =
        createField(DSL.name(fieldName), SQLDataType.VARCHAR)

    protected fun createVarcharArrayField(fieldName: String): TableField<Array<String>> =
        createField(DSL.name(fieldName), SQLDataType.VARCHAR.arrayDataType)

    protected fun createTimestampField(fieldName: String): TableField<Timestamp> =
        createField(DSL.name(fieldName), SQLDataType.TIMESTAMP)

    protected fun createInstantField(fieldName: String): TableField<Instant> =
        createField(DSL.name(fieldName), KotlinJooq.instantType)

    protected  fun createLocalTimeField(fieldName:String): TableField<LocalTime> =
        createField(DSL.name(fieldName), SQLDataType.LOCALTIME)

    protected fun createDateField(fieldName: String): TableField<Date> =
        createField(DSL.name(fieldName), SQLDataType.DATE)

    protected fun createLocalDateField(fieldName: String): TableField<LocalDate> =
        createField(DSL.name(fieldName), SQLDataType.LOCALDATE)

    protected  fun createLocalDateTimeField(fieldName:String): TableField<LocalDateTime> =
        createField(DSL.name(fieldName), SQLDataType.LOCALDATETIME)

    protected fun createBooleanField(fieldName: String): TableField<Boolean> =
        createField(DSL.name(fieldName), SQLDataType.BOOLEAN)

    protected fun createJsonField(fieldName: String): TableField<JsonNode> =
        createField(DSL.name(fieldName), KotlinJooq.jacksonNodeType)

    protected fun createJsonObjectField(fieldName: String): TableField<ObjectNode> =
        createField(DSL.name(fieldName), KotlinJooq.jacksonObjectType)

    protected fun <T> createConcreteJsonField(fieldName: String, clazz: Class<T>): TableField<T> =
        createField(DSL.name(fieldName), jacksonConcreteType(clazz))

    protected fun createUUIDField(fieldName: String): TableField<UUID> =
        createField(DSL.name(fieldName), SQLDataType.UUID)

    protected fun <DBT, T> createConvertedField(fieldName: String, dataType: DataType<DBT>, converter: Converter<in DBT, T>): TableField<T> =
        createField(DSL.name(fieldName),dataType.asConvertedDataType(converter))
}


