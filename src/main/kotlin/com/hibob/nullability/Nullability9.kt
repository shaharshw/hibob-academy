package com.hibob.nullability

/**
 * Filter Departments: Identify departments that have either no manager assigned or where the manager's
 * contact information is entirely missing.
 *
 * Email List Compilation: Generate a list of all unique manager emails but exclude any null
 * or empty strings. Ensure the list has no duplicates.
 *
 * Reporting: For each department, generate a detailed report that includes the
 * department name, manager's name, email, and formatted phone number.
 * If any information is missing, provide a placeholder.
 *
 */

data class DepartmentData(val name: String?, val manager: EmployeeData?)
data class EmployeeData(val name: String?, val contactInfo: Contact?)
data class Contact(val email: String?, val phone: String?)

fun filterDepartmentsWithoutValidManagers(departments: List<DepartmentData>): List<String> {
    return departments
        .filter { department ->
            val manager = department.manager
            val contactInfo = manager?.contactInfo
            contactInfo?.email.isNullOrBlank() || contactInfo?.phone.isNullOrBlank()
        }
        .map { department ->
            department.name ?: "Unknown Department"
        }
}

fun generateUniqueEmailsList(departments: List<DepartmentData>): Set<String> {
    return departments
        .mapNotNull { department ->
            department.manager?.contactInfo?.email
        }
        .filter { email ->
            email.isNotBlank()
        }
        .toSet()
}

fun reportDepartment(departments: List<DepartmentData>)  {

    val report = departments
        .forEach { department ->
            val departmentName = department.name ?: "Name not provided"
            val employeeData = department.manager?.let {

            }
        }
}

fun main() {
    val departments = listOf(
        DepartmentData("Engineering", EmployeeData("Alice", Contact("alice@example.com", "123-456-7890"))),
        DepartmentData("Human Resources", null),
        DepartmentData("Operations", EmployeeData("Bob", Contact(null, "234-567-8901"))),
        DepartmentData("Marketing", EmployeeData(null, Contact("marketing@example.com", "345-678-9012"))),
        DepartmentData("Finance", EmployeeData("Carol", Contact("", "456-789-0123")))
    )

    val departmentNames = filterDepartmentsWithoutValidManagers(departments)
    departmentNames
        .forEach { departmentName ->
            println(departmentName)
        }

    println("----------------------------")

    val uniqueEmail = generateUniqueEmailsList(departments)
    uniqueEmail
        .forEach { email ->
            println(email)
        }


    println("----------------------------")





}

