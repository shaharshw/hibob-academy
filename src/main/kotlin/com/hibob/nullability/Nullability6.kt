package com.hibob.nullability

data class Department(val name: String?, val manager: EmployeeDetails?)
data class EmployeeDetails(val name: String?, val contactInfo: ContactInfo?)
data class ContactInfo(val email: String?, val phone: String?)

fun main() {
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
        val managerName = department.manager?.name ?: "Name not provided"
        val managerEmail = department.manager?.contactInfo?.email ?: "Email not provided"
        val managerPhone = department.manager?.contactInfo?.phone ?: "Phone not provided"

        println("Department: $departmentName, Manager: $managerName, Email: $managerEmail, Phone: $managerPhone")
    }
}
