package com.hibob.nullability

/**
 * Modify the main function to calculate and print the sum of all non-null integers in the list numbers.
 * Use safe calls and/or the Elvis operator where appropriate.
 **/
fun main5() {
    val numbers: List<Int?> = listOf(1, null, 3, null, 5)
    // Task: Calculate the sum of all non-null numbers

    var sum = 0

    numbers
        .forEach {
            sum += it ?: 0
        }

    println("$sum")

}
