package com.hibob.prop

import java.time.DayOfWeek

//1. create class called Store that initlize by day and list of products
//2. add property to that indicate if the store is open the store is open all the day expect saturday
//3. add property to that indicate number of product
//4. add val that get receipts //no need to implement the method but its an heavy task
//5. I want to count number of calling get receipts
//6. write vairable that initilize only when calling create method

class Store(
    val day : DayOfWeek,
    val products : List<String>
) {

    val isOpen : Boolean
        get() = day != DayOfWeek.SATURDAY

    val numberOfProducts : Int
        get() = products.size

    private val lazyReceipts : List<String> by lazy {
        println("heavy task")
        listOf("receipt1", "receipt2")
    }

    val receipts : List<String>
        get() {
            getReceiptsCount++
            return lazyReceipts
        }

    var getReceiptsCount : Int = 0

    lateinit var create : String

    fun create() {
        create = "created"
    }
}

//fun main() {
//    val store = Store(DayOfWeek.MONDAY, listOf("product1", "product2"))
//    println(store.isOpen)
//    println(store.numberOfProducts)
//    println(store.receipts)
//    println(store.receipts)
//    println(store.getReceiptsCount)
//    store.create()
//    println(store.create)
//
//    println("----------------------")
//
//    val store2 = Store(DayOfWeek.SATURDAY, listOf("product1", "product2"))
//    println(store2.isOpen)
//    println(store2.receipts)
//    println(store2.getReceiptsCount)
//    println(store2.receipts)
//    println(store2.getReceiptsCount)
//}


