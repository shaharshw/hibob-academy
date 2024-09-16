package com.hibob.nullability

/**
 * Instructions:
 *
 * Traverse through the company structure starting from departments to teams and finally to team members.
 * For each level (company, department, team, leader, members), check for null values and print appropriate information.
 * Ensure that every piece of information printed includes a fallback for null values using the Elvis operator and that
 * blocks of code dependent on non-null values are executed using ?.let.
 *
 */
data class Company(val name: String?, val departments: List<DepartmentDetails?>?)
data class DepartmentDetails(val name: String?, val teams: List<Team?>?)
data class Team(val name: String?, val leader: Leader?, val members: List<Member?>?)
data class Leader(val name: String?, val title: String?)
data class Member(val name: String?, val role: String?)

fun initializeCompany(): Company {
    return Company(
        "Tech Innovations Inc.",
        listOf(
            DepartmentDetails("Engineering", listOf(
                Team("Development", Leader("Alice Johnson", "Senior Engineer"), listOf(Member("Bob Smith", "Developer"), null)),
                Team("QA", Leader(null, "Head of QA"), listOf(Member(null, "QA Analyst"), Member("Eve Davis", null))),
                null
            )),
            DepartmentDetails(null, listOf(
                Team("Operations", null, listOf(Member("John Doe", "Operator"), Member("Jane Roe", "Supervisor")))
            )),
            null
        )
    )
}

fun main6() {
    val company = initializeCompany()

    // Print detailed information about each department, team, and team members, handling all null values appropriately.
    val companyName = company.name ?: "Name not provided"
    println("Company: $companyName")

    company.departments?.forEachIndexed { departmentIndex, department ->
        val departmentName = department?.name ?: "Name of department not provided"
        println("Department ${departmentIndex + 1}: $departmentName")

        department?.teams?.forEachIndexed { teamIndex, team ->
            val teamName = team?.name ?: "Name of team not provided"
            val leaderInfo = team?.leader?.let { leader ->
                val leaderName = leader.name ?: "Name of leader not provided"
                val leaderTitle = leader.title ?: "Title of leader not provided"
                "Leader name: $leaderName, Title: $leaderTitle"
            } ?: "Leader not provided"

            val membersInfo = team?.members?.joinToString(separator = "; ") { member ->
                val memberName = member?.name ?: "Name of member not provided"
                val memberRole = member?.role ?: "Role of member not provided"
                "Name: $memberName, Role: $memberRole"
            } ?: "Members not provided"

            println("  Team ${teamIndex + 1}: $teamName, $leaderInfo, Members: $membersInfo")
        } ?: println("  Teams not provided")
    } ?: println("Departments not provided")
}