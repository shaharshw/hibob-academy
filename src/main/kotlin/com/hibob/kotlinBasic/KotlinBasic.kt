package com.hibob.kotlinBasic

fun helloWorld() = println("Hello World")

fun evenOrOdd(number : Int) : String {

    return if (number % 2 == 0) "Even" else "Odd"
}

fun equalOrNot(number1 : Int, number2 : Int) : Boolean = number1 == number2

fun max(number1 : Int, number2 : Int) : Int = if (number1 > number2) number1 else number2

fun multiplication(a: Int = 1, b: Int = 1): Int = a * b

fun main() {

    helloWorld()

    println("-----------------")

    println(evenOrOdd(5))
    println(evenOrOdd(4))

    println("-----------------")

    println(equalOrNot(5,5))
    println(equalOrNot(4,5))

    println("-----------------")

    println(max(4,5))

    println("-----------------")
    //a = 1, b= 5
    println(multiplication(b = 5))
    println(multiplication(a = 1, b = 5))
    //a = 1, b= 1
    println(multiplication(b = 1))
    println(multiplication(a = 1, b = 1))
    // a=3, b= 2
    println(multiplication(a = 3, b = 2))

}