package com.hibob.academy.utils

import org.jooq.Field
import org.jooq.Record
import org.jooq.RecordMapper
import kotlin.reflect.*

interface FieldExtractor<T> {
    fun extract(record: Record): T

    fun fields(): List<Field<*>>

    companion object {
        fun <T> constant(value: T): FieldExtractor<T> =
            object : FieldExtractor<T> {
                override fun extract(record: Record): T = value

                override fun fields(): List<Field<*>> = emptyList()
            }
    }
}

fun <T> Field<T>.asIs(): FieldExtractor<T> = map { v -> v }

fun <T, T1> Field<T>.map(mapper: (T) -> T1): FieldExtractor<T1> = FieldMapper(this, mapper)

data class FieldMapper<DBT, T>(val field: Field<DBT>, val mapper: (DBT) -> T) : FieldExtractor<T> {
    override fun extract(record: Record): T = mapper(record[field])

    override fun fields(): List<Field<*>> = listOf(field)

    private fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C = {
        f(g(it))
    }

    fun <T1> map(mapper: (T) -> T1): FieldMapper<DBT, T1> = FieldMapper(field, compose(mapper, this.mapper))
}

fun <T, T0> KFunction1<T0, T>.from(
    f0: Field<T0>
): DataClassMapper<T> = this.from(f0.asIs())

fun <T, T0> KFunction1<T0, T>.from(
    f0: FieldExtractor<T0>,
): DataClassMapper<T> =
    GenericDataClassMapper({ args ->
        @Suppress("UNCHECKED_CAST")
        this(args[0] as T0)
    }, listOf(f0))

fun <T, T0, T1> KFunction2<T0, T1, T>.from(
    f0: Field<T0>, f1: Field<T1>
): DataClassMapper<T> = this.from(f0.asIs(), f1.asIs())

fun <T, T0, T1> KFunction2<T0, T1, T>.from(
    f0: FieldExtractor<T0>,
    f1: FieldExtractor<T1>,
): DataClassMapper<T> =
    GenericDataClassMapper({ args ->
        @Suppress("UNCHECKED_CAST")
        this(args[0] as T0, args[1] as T1)
    }, listOf(f0, f1))

fun <T, T0, T1, T2> KFunction3<T0, T1, T2, T>.from(
    f0: Field<T0>, f1: Field<T1>, f2: Field<T2>
): DataClassMapper<T> = this.from(f0.asIs(), f1.asIs(), f2.asIs())

fun <T, T0, T1, T2> KFunction3<T0, T1, T2, T>.from(
    f0: FieldExtractor<T0>,
    f1: FieldExtractor<T1>,
    f2: FieldExtractor<T2>
): DataClassMapper<T> =
    GenericDataClassMapper({ args ->
        @Suppress("UNCHECKED_CAST")
        this(args[0] as T0, args[1] as T1, args[2] as T2)
    }, listOf(f0, f1, f2))

fun <T, T0, T1, T2, T3> KFunction4<T0, T1, T2, T3, T>.from(
    f0: Field<T0>, f1: Field<T1>, f2: Field<T2>, f3: Field<T3>
): DataClassMapper<T> = this.from(f0.asIs(), f1.asIs(), f2.asIs(), f3.asIs())

fun <T, T0, T1, T2, T3> KFunction4<T0, T1, T2, T3, T>.from(
    f0: FieldExtractor<T0>,
    f1: FieldExtractor<T1>,
    f2: FieldExtractor<T2>,
    f3: FieldExtractor<T3>,
): DataClassMapper<T> =
    GenericDataClassMapper({ args ->
        @Suppress("UNCHECKED_CAST")
        this(args[0] as T0, args[1] as T1, args[2] as T2, args[3] as T3)
    }, listOf(f0, f1, f2, f3))

fun <T, T0, T1, T2, T3, T4> KFunction5<T0, T1, T2, T3, T4, T>.from(
    f0: Field<T0>, f1: Field<T1>, f2: Field<T2>, f3: Field<T3>, f4: Field<T4>
): DataClassMapper<T> = this.from(f0.asIs(), f1.asIs(), f2.asIs(), f3.asIs(), f4.asIs())

fun <T, T0, T1, T2, T3, T4> KFunction5<T0, T1, T2, T3, T4, T>.from(
    f0: FieldExtractor<T0>,
    f1: FieldExtractor<T1>,
    f2: FieldExtractor<T2>,
    f3: FieldExtractor<T3>,
    f4: FieldExtractor<T4>,
): DataClassMapper<T> =
    GenericDataClassMapper({ args ->
        @Suppress("UNCHECKED_CAST")
        this(args[0] as T0, args[1] as T1, args[2] as T2, args[3] as T3, args[4] as T4)
    }, listOf(f0, f1, f2, f3, f4))

fun <T, T0, T1, T2, T3, T4, T5> KFunction6<T0, T1, T2, T3, T4, T5, T>.from(
    f0: FieldExtractor<T0>,
    f1: FieldExtractor<T1>,
    f2: FieldExtractor<T2>,
    f3: FieldExtractor<T3>,
    f4: FieldExtractor<T4>,
    f5: FieldExtractor<T5>,
): DataClassMapper<T> =
    GenericDataClassMapper({ args ->
        @Suppress("UNCHECKED_CAST")
        this(args[0] as T0, args[1] as T1, args[2] as T2, args[3] as T3, args[4] as T4, args[5] as T5)
    }, listOf(f0, f1, f2, f3, f4, f5))

fun <T, T0, T1, T2, T3, T4, T5, T6> KFunction7<T0, T1, T2, T3, T4, T5, T6, T>.from(
    f0: FieldExtractor<T0>,
    f1: FieldExtractor<T1>,
    f2: FieldExtractor<T2>,
    f3: FieldExtractor<T3>,
    f4: FieldExtractor<T4>,
    f5: FieldExtractor<T5>,
    f6: FieldExtractor<T6>
): DataClassMapper<T> =
    GenericDataClassMapper({ args ->
        @Suppress("UNCHECKED_CAST")
        this(args[0] as T0, args[1] as T1, args[2] as T2, args[3] as T3, args[4] as T4, args[5] as T5, args[6] as T6)
    }, listOf(f0, f1, f2, f3, f4, f5, f6))

fun <T, T0, T1, T2, T3, T4, T5, T6, T7> KFunction8<T0, T1, T2, T3, T4, T5, T6, T7, T>.from(
    f0: FieldExtractor<T0>,
    f1: FieldExtractor<T1>,
    f2: FieldExtractor<T2>,
    f3: FieldExtractor<T3>,
    f4: FieldExtractor<T4>,
    f5: FieldExtractor<T5>,
    f6: FieldExtractor<T6>,
    f7: FieldExtractor<T7>,
): DataClassMapper<T> =
    GenericDataClassMapper({ args ->
        @Suppress("UNCHECKED_CAST")
        this(args[0] as T0, args[1] as T1, args[2] as T2, args[3] as T3, args[4] as T4, args[5] as T5, args[6] as T6,
            args[7] as T7)
    }, listOf(f0, f1, f2, f3, f4, f5, f6, f7))

fun <T, T0, T1, T2, T3, T4, T5, T6, T7, T8> KFunction9<T0, T1, T2, T3, T4, T5, T6, T7, T8, T>.from(
    f0: FieldExtractor<T0>,
    f1: FieldExtractor<T1>,
    f2: FieldExtractor<T2>,
    f3: FieldExtractor<T3>,
    f4: FieldExtractor<T4>,
    f5: FieldExtractor<T5>,
    f6: FieldExtractor<T6>,
    f7: FieldExtractor<T7>,
    f8: FieldExtractor<T8>,
): DataClassMapper<T> =
    GenericDataClassMapper({ args ->
        @Suppress("UNCHECKED_CAST")
        this(args[0] as T0, args[1] as T1, args[2] as T2, args[3] as T3, args[4] as T4, args[5] as T5, args[6] as T6,
            args[7] as T7, args[8] as T8)
    }, listOf(f0, f1, f2, f3, f4, f5, f6, f7, f8))

fun <T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> KFunction10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T>.from(
    f0: FieldExtractor<T0>,
    f1: FieldExtractor<T1>,
    f2: FieldExtractor<T2>,
    f3: FieldExtractor<T3>,
    f4: FieldExtractor<T4>,
    f5: FieldExtractor<T5>,
    f6: FieldExtractor<T6>,
    f7: FieldExtractor<T7>,
    f8: FieldExtractor<T8>,
    f9: FieldExtractor<T9>,
): DataClassMapper<T> =
    GenericDataClassMapper({ args ->
        @Suppress("UNCHECKED_CAST")
        this(args[0] as T0, args[1] as T1, args[2] as T2, args[3] as T3, args[4] as T4, args[5] as T5, args[6] as T6,
            args[7] as T7, args[8] as T8, args[9] as T9)
    }, listOf(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9))
fun <T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> KFunction11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T>.from(
    f0: FieldExtractor<T0>,
    f1: FieldExtractor<T1>,
    f2: FieldExtractor<T2>,
    f3: FieldExtractor<T3>,
    f4: FieldExtractor<T4>,
    f5: FieldExtractor<T5>,
    f6: FieldExtractor<T6>,
    f7: FieldExtractor<T7>,
    f8: FieldExtractor<T8>,
    f9: FieldExtractor<T9>,
    f10: FieldExtractor<T10>,
): DataClassMapper<T> =
    GenericDataClassMapper({ args ->
        @Suppress("UNCHECKED_CAST")
        this(args[0] as T0, args[1] as T1, args[2] as T2, args[3] as T3, args[4] as T4, args[5] as T5, args[6] as T6,
            args[7] as T7, args[8] as T8, args[9] as T9, args[10] as T10)
    }, listOf(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10))

fun <T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> KFunction12<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T>.from(
    f0: FieldExtractor<T0>,
    f1: FieldExtractor<T1>,
    f2: FieldExtractor<T2>,
    f3: FieldExtractor<T3>,
    f4: FieldExtractor<T4>,
    f5: FieldExtractor<T5>,
    f6: FieldExtractor<T6>,
    f7: FieldExtractor<T7>,
    f8: FieldExtractor<T8>,
    f9: FieldExtractor<T9>,
    f10: FieldExtractor<T10>,
    f11: FieldExtractor<T11>,
): DataClassMapper<T> =
    GenericDataClassMapper({ args ->
        @Suppress("UNCHECKED_CAST")
        this(args[0] as T0, args[1] as T1, args[2] as T2, args[3] as T3, args[4] as T4, args[5] as T5, args[6] as T6,
            args[7] as T7, args[8] as T8, args[9] as T9, args[10] as T10, args[11] as T11)
    }, listOf(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11))

fun <T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> KFunction13<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T>.from(
    f0: FieldExtractor<T0>,
    f1: FieldExtractor<T1>,
    f2: FieldExtractor<T2>,
    f3: FieldExtractor<T3>,
    f4: FieldExtractor<T4>,
    f5: FieldExtractor<T5>,
    f6: FieldExtractor<T6>,
    f7: FieldExtractor<T7>,
    f8: FieldExtractor<T8>,
    f9: FieldExtractor<T9>,
    f10: FieldExtractor<T10>,
    f11: FieldExtractor<T11>,
    f12: FieldExtractor<T12>,
): DataClassMapper<T> =
    GenericDataClassMapper({ args ->
        @Suppress("UNCHECKED_CAST")
        this(args[0] as T0, args[1] as T1, args[2] as T2, args[3] as T3, args[4] as T4, args[5] as T5, args[6] as T6,
            args[7] as T7, args[8] as T8, args[9] as T9, args[10] as T10, args[11] as T11, args[12] as T12)
    }, listOf(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12))

interface DataClassMapper<T> : RecordMapper<Record, T>, FieldExtractor<T> {
    override fun map(record: Record): T

    override fun extract(record: Record): T = map(record)

    fun nullableByField(field: Field<*>): DataClassMapper<T?> =
        NullableDataClassMapper(field, this)

    companion object {
        fun <T, T0> of(ctor: (p0: T0) -> T, f0: FieldExtractor<T0>): DataClassMapper<T> =
            GenericDataClassMapper({ args ->
                @Suppress("UNCHECKED_CAST")
                ctor(args[0] as T0)
            }, listOf(f0))

        fun <T, T0, T1> of(ctor: (p0: T0, p1: T1) -> T, f0: FieldExtractor<T0>, f1: FieldExtractor<T1>): DataClassMapper<T> =
            GenericDataClassMapper({ args ->
                @Suppress("UNCHECKED_CAST")
                ctor(args[0] as T0, args[1] as T1)
            }, listOf(f0, f1))

        fun <T, T0, T1, T2> of(
            ctor: (p0: T0, p1: T1, p2: T2) -> T,
            f0: FieldExtractor<T0>,
            f1: FieldExtractor<T1>,
            f2: FieldExtractor<T2>,
        ): DataClassMapper<T> =
            GenericDataClassMapper({ args ->
                @Suppress("UNCHECKED_CAST")
                ctor(args[0] as T0, args[1] as T1, args[2] as T2)
            }, listOf(f0, f1, f2))

        fun <T, T0, T1, T2, T3> of(
            ctor: (p0: T0, p1: T1, p2: T2, p3: T3) -> T,
            f0: FieldExtractor<T0>,
            f1: FieldExtractor<T1>,
            f2: FieldExtractor<T2>,
            f3: FieldExtractor<T3>,
        ): DataClassMapper<T> =
            GenericDataClassMapper({ args ->
                @Suppress("UNCHECKED_CAST")
                ctor(args[0] as T0, args[1] as T1, args[2] as T2, args[3] as T3)
            }, listOf(f0, f1, f2, f3))

        fun <T, T0, T1, T2, T3, T4> of(
            ctor: (p0: T0, p1: T1, p2: T2, p3: T3, p4: T4) -> T,
            f0: FieldExtractor<T0>,
            f1: FieldExtractor<T1>,
            f2: FieldExtractor<T2>,
            f3: FieldExtractor<T3>,
            f4: FieldExtractor<T4>,
        ): DataClassMapper<T> =
            GenericDataClassMapper({ args ->
                @Suppress("UNCHECKED_CAST")
                ctor(args[0] as T0, args[1] as T1, args[2] as T2, args[3] as T3, args[4] as T4)
            }, listOf(f0, f1, f2, f3, f4))

        fun <T, T0, T1, T2, T3, T4, T5> of(
            ctor: (p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5) -> T,
            f0: FieldExtractor<T0>,
            f1: FieldExtractor<T1>,
            f2: FieldExtractor<T2>,
            f3: FieldExtractor<T3>,
            f4: FieldExtractor<T4>,
            f5: FieldExtractor<T5>,
        ): DataClassMapper<T> =
            GenericDataClassMapper({ args ->
                @Suppress("UNCHECKED_CAST")
                ctor(args[0] as T0, args[1] as T1, args[2] as T2, args[3] as T3, args[4] as T4, args[5] as T5)
            }, listOf(f0, f1, f2, f3, f4, f5))
    }
}

data class GenericDataClassMapper<T>(val ctorFromArray: (Array<*>) -> T, val extractors: List<FieldExtractor<*>>) :
    DataClassMapper<T> {
    override fun map(record: Record): T =
        ctorFromArray(extractors.map { it.extract(record) }.toTypedArray())

    override fun fields() = extractors.flatMap { it.fields() }
}

data class NullableDataClassMapper<T>(val nullableField: Field<*>, val delegate: DataClassMapper<T>) :
    DataClassMapper<T?> {
    override fun map(record: Record): T? =
        record.get(nullableField)?.let { delegate.map(record) }

    override fun fields(): List<Field<*>> =
        delegate.fields() + nullableField
}
