package com.hibob.oop

data class Participant(
    val name : String,
    val email : String
)

open class Meeting(
    open val name : String,
    open val location : Location,
    private val participants : List<Participant>
) {

    open fun addParticipant(participant: Participant) : Meeting =
        Meeting(name, location, participants + participant)

    override fun toString(): String {
        return "Meeting(name='$name', location=$location, participants=$participants)"
    }
}

data class PersonalReview (
    override val name : String,
    override val location : Location,
    val participant: Participant,
    val reviewers: List<Participant>
) : Meeting(name, location, listOf(participant) + reviewers)
{
    init {
        println(successMessage())
    }

    private fun successMessage(): String {
        return "Personal Review for $name created successfully."
    }
}



