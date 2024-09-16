package com.hibob.nullability

data class Department(val name: String?, val manager: EmployeeDetails?)
data class EmployeeDetails(val name: String?, val contactInfo: ContactInfo?)
data class ContactInfo(val email: String?, val phone: String?)

fun main2() {
    val departments = listOf(
        Department("Engineering", EmployeeDetails("Alice", ContactInfo("alice@example.com", null))),
        Department("Human Resources", null),
        Department(null, EmployeeDetails("Bob", ContactInfo(null, "123-456-7890"))),
        Department("Marketing", EmployeeDetails(null, ContactInfo("marketing@example.com", "987-654-3210")))
    )

    // Task: Print each department's name and manager's contact information.
    // If any information is missing, use appropriate defaults.
    departments.forEach { department ->
        val departmentName = department.name ?: "Name not provided"

        val managerInfo = department.manager?.let { manager ->
            val managerName = manager.name ?: "Name not provided"
            val managerEmail = manager.contactInfo?.email ?: "Email not provided"
            val managerPhone = manager.contactInfo?.phone ?: "Phone not provided"

            "Manager: $managerName, Email: $managerEmail, Phone: $managerPhone"
        } ?: "Manager: Manager not provided"

        println("Department: $departmentName, $managerInfo")
    }
}
