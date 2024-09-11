package com.hibob.unitest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Write unit tests to verify that the deposit and withdraw methods function correctly.
 * Handle edge cases, such as invalid inputs (e.g., negative amounts).
 * Ensure that the getBalance method returns the correct balance after a series of deposits and withdrawals.
 */

class BankAccountTest {

    lateinit var  bankAccount : BankAccount

    @BeforeEach
    fun setUp() {
        bankAccount = BankAccount(0.0)
    }

    @Test
    fun `deposit valid amount increases balance`() {

        val toDeposit = 100.0
        assertEquals(toDeposit, bankAccount.deposit(toDeposit))
    }

    @Test
    fun `deposit negative or zero amount throws IllegalArgumentException`() {

        val toDeposit = -100.0
        assertThrows(IllegalArgumentException::class.java) {
            bankAccount.deposit(toDeposit)
        }
    }

    @Test
    fun `withdraw valid amount decreases balance`() {

            val toDeposit = 100.0
            bankAccount.deposit(toDeposit)
            val toWithdraw = 50.0
            assertEquals(toDeposit - toWithdraw, bankAccount.withdraw(toWithdraw))
    }

    @Test
    fun `withdraw amount greater than balance throws IllegalArgumentException`() {

        val toDeposit = 20.0
        bankAccount.deposit(toDeposit)
        val toWithdraw = 100.0
        assertThrows(IllegalArgumentException::class.java) {
            bankAccount.withdraw(toWithdraw)
        }
    }

    @Test
    fun `withdraw negative or zero amount throws IllegalArgumentException`() {

            val toDeposit = 20.0
            bankAccount.deposit(toDeposit)
            val toWithdraw = -100.0
            assertThrows(IllegalArgumentException::class.java) {
                bankAccount.withdraw(toWithdraw)
            }
    }

    @Test
    fun `getBalance returns the correct balance`() {

            val toDeposit = 100.0
            bankAccount.deposit(toDeposit)
            val toWithdraw = 50.0
            bankAccount.withdraw(toWithdraw)
            assertEquals(toDeposit - toWithdraw, bankAccount.getBalance())
    }
}
