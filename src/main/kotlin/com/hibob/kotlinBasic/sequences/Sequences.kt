package com.hibob.kotlinBasic.sequences


fun ex1() {
    // change the program to
    // 1. reuse the filter / map function
    // 2. println each call to track the diffs between List and Seq

    val list = listOf(1, 2, 3, 4)

    println("List:")
    val maxOddSquare = list
        .map { powerOfTwo(it) }
        .filter { isEven(it) }
        .find { it == 4 }

    println("-------------------------")

    println("Sequence:")
    val maxOddSquare2 = list.asSequence()
        .map { powerOfTwo(it) }
        .filter { isEven(it) }
        .find { it == 4 }

}

private fun powerOfTwo(number: Int): Int {
    println("powerOfTwo was called with number: $number")
    return number * number
}

private fun isEven(number: Int): Boolean {
    println("isEven was called with number: $number")
    return number % 2 == 0
}

fun ex2() {
    // how many times filterFunc was called
    fun filterFunc(it: Int): Boolean {
        println("filterFunc was called")
        return it < 3
    }
    sequenceOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .filter { filterFunc(it) }
}

fun ex3() {
    // create the list of the first 10 items of (2, 4, 8, 16, 32 ...) seq

    val list = generateSequence(2) { it * 2 }
        .take(10)
        .toList()

    println(list)
}

fun ex4() {
    // create the list of the first 10 items of the Fibonacci seq
    val fibonacciSeq = sequence {
        var a = 1
        yield(a)

        var b = 1
        yield(b)

        while (true) {
            val c = a + b
            yield(c)
            a = b
            b = c
        }
    }

    val fibonacciList = fibonacciSeq
        .take(10)
        .toList()

    println(fibonacciList)


}

fun ex5() {
    // try to minimize the number of operations:
    var count = 0
    val engToHeb: Map<String, String> = mapOf("today" to "f", "was" to "r", "a" to "q", "good" to "go", "day" to "da") // assume the dictionary is real :-)
    val b = "today was a good day for walking in the part. Sun was shining and birds were chirping"
        .splitToSequence(" ")
        .mapNotNull { engToHeb[it] }
        .filter {
            println("Filtering : ${++count}")
            it.length <= 3
        }
        .take(5)
        .count() > 4

    println(b)
}

//fun main() {
//
//    ex1()
//    ex2()
//    ex3()
//    ex4()
//    ex5()
//}