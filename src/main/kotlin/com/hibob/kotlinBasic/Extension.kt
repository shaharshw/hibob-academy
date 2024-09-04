package com.hibob.kotlinBasic

fun List<Int>.sum() : Int {

        var result = 0
        this.map { number ->
            result += number
        }

        return result
    }

infix fun Number.toPowerOf(exponent : Number) : Double {

    return Math.pow(this.toDouble(), exponent.toDouble())
}

fun main(args : Array<String>) {

    val sum = listOf(1,2,3).sum()
    println(sum)

    val number = 2
    var result = number.toPowerOf(2)
    println(result)

    result = number.toPowerOf(4)
    println(result)

}