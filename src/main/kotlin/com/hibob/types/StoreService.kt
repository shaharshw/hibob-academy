package com.hibob.types

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class StoreService {

    fun pay(cart: List<Cart>, payment: Payment): Map<String, Check> {

        return cart.associate {
            it.clientId to checkOut(it, payment)
        }
    }

    private fun checkOut(cart: Cart, payment: Payment): Check {

        val totalPrice = calculateTotalPrice(cart)

        val status = isPaymentValid(payment, totalPrice)

        val finalPrice = if (status == Statuses.SUCCESS) totalPrice else 0.0

        return Check(cart.clientId, status, finalPrice)
    }

    private fun fail(message: String): Nothing {
        throw IllegalStateException(message)
    }

    private fun checkProduct(product: Product): Boolean {
        return product.custom == true
    }

    private fun calculateTotalPrice(cart: Cart): Double {
        return cart.products
            .filter { checkProduct(it) }
            .sumOf { it.price }
    }

    private fun isPaymentValid(payment: Payment, totalPrice: Double): Statuses {

        return when (payment) {

            is Payment.CreditCard -> {
                if (isCreditCardValid(payment, totalPrice)) Statuses.SUCCESS else Statuses.FAILURE
            }

            is Payment.PayPal -> {
                if (isPayPalValid(payment)) Statuses.SUCCESS else Statuses.FAILURE
            }

            is Payment.Cash -> fail("Cash payment is not allowed")
        }
    }

    private fun isCreditCardValid(payment: Payment.CreditCard, totalPrice: Double) : Boolean =
        isSupportedType(payment.type) && isCardDetailsValid(payment) && isNotOverTheLimit(totalPrice, payment)

    private fun isSupportedType(cardType: CreditCardType) : Boolean =
        cardType == CreditCardType.MASTERCARD || cardType == CreditCardType.VISA

    private fun isCardDetailsValid(payment: Payment.CreditCard) : Boolean =
        payment.expiryDate.isAfter(LocalDate.now()) && payment.number.length == 10

    private fun isNotOverTheLimit(price: Double, payment: Payment.CreditCard) : Boolean =
        price < payment.limit

    private fun isPayPalValid(payment: Payment.PayPal): Boolean {
        return payment.email.contains('@')
    }
}