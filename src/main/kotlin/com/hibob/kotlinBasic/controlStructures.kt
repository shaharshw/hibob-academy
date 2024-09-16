package com.hibob.kotlinBasic

fun isValidIdentifier(s: String): Boolean {

    if (s.isEmpty()) return false
    if (!isLetter(s[0]) && s[0] != '_') return false

    for (c in s) {
        if (isNotLetterOrDigit(c) && c != '_') {
            return false
        }
    }

    return true
}

fun isLetter(c : Char) : Boolean = (c in 'a'..'z' || c in 'A'..'Z')

fun isNotLetterOrDigit(c : Char) : Boolean = !isLetter(c) && c !in '0'..'9'

//fun main(args: Array<String>) {
//    println(isValidIdentifier("name"))   // true
//    println(isValidIdentifier("_name"))  // true
//    println(isValidIdentifier("_12"))    // true
//    println(isValidIdentifier(""))       // false
//    println(isValidIdentifier("012"))    // false
//    println(isValidIdentifier("no$"))    // false
//    println(isValidIdentifier("34shahar"))    // false
//    println(isValidIdentifier("shahar34"))    // true
//}
