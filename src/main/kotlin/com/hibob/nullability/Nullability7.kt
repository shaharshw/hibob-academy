package com.hibob.nullability

/**
 *
 * Iterate through the list of customers, which may contain null entries.
 * For each non-null customer, print the customer's name or "Name Unknown" if the name is null.
 * Print the account ID or "Account ID Unknown" if the account or ID is null.
 * Print the account balance or "Balance Not Available" if the account details or balance is null.
 * If the customer object itself is null, print "Customer data is not available."
 *
 */
data class Customer(val name: String?, val account: Account?)
data class Account(val id: String?, val details: AccountDetails?)
data class AccountDetails(val type: String?, val balance: Double?)

fun initializeNullableCustomers(): List<Customer?> {
    return listOf(
        Customer("John Doe", Account("12345", AccountDetails("Checking", 1500.00))),
        null,
        Customer("Jane Smith", Account("67890", AccountDetails(null, 780.00))),
        Customer(null, Account(null, AccountDetails("Savings", null))),
        null,
        Customer("Emily White", null)
    )
}

fun main1() {
    val customers = initializeNullableCustomers()

    // Task: Print each customer's name, account ID, and account balance. Handle all null cases appropriately.

    customers
        .forEach { customer ->
            customer?.let {
                val customerName = customer.name ?: "Name Unknown"
                val accountInfo = customer.account?.let { account ->
                    val accountId = account.id ?: "Account ID Unknown"
                    val accountType = account.details?.type ?: "Type Unknown"
                    val accountBalance = account.details?.balance ?: "Balance Not Available"

                    "Account ID: $accountId, Account Type: $accountType, Balance: $accountBalance\""
                } ?: "Account Unknown"


                println("Customer: $customerName, Account: $accountInfo")

            } ?: println("Customer data is not available.")
        }
}
