package com.hibob.types

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class StoreService {

    fun pay(cart: List<Cart>, payment: Payment): Map<String, Check> {

        return cart.associate { cart ->
            cart.clientId to checkOut(cart, payment) }
    }

    fun checkOut(cart : Cart, payment: Payment): Check {

        val totalPrice = calculateTotalPrice(listOf(cart))

        val status = isPaymentValid(payment, totalPrice, LocalDate.now())

        val finalPrice = if (status == Statuses.SUCCESS) totalPrice else 0.0

        return Check(cart.clientId, status, finalPrice)
    }

    private fun fail(message: String): Nothing {
        throw IllegalStateException(message)
    }

    private fun checkProduct(product: Product): Boolean {
        return product.custom == true
    }

    private fun calculateTotalPrice(cart: List<Cart>): Double {
        return cart
            .flatMap { it.products }
            .filter { checkProduct(it) }
            .sumOf { it.price }
    }

    private fun isPaymentValid(payment: Payment, totalPrice: Double, now : LocalDate): Statuses {

        return when (payment) {

            is Payment.CreditCard -> {
                if (isCreditCardValid(payment, totalPrice, now)) Statuses.SUCCESS else Statuses.FAILURE
            }

            is Payment.PayPal -> {
                if(isPayPalValid(payment)) Statuses.SUCCESS else Statuses.FAILURE
            }

            is Payment.Cash -> fail("Cash payment is not allowed")
        }
    }

    private fun isCreditCardValid(payment: Payment.CreditCard, totalPrice: Double, now : LocalDate): Boolean {
        return ((payment.type == CreditCardType.VISA || payment.type == CreditCardType.MASTERCARD) &&
                (payment.expiryDate.isAfter(now)) && (payment.limit > totalPrice) && (payment.number.length == 10))
    }

    private fun isPayPalValid(payment: Payment.PayPal): Boolean {
        return payment.email.contains("@")
    }
}