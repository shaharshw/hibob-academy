package com.hibob.oop

data class Participant(
    val name : String,
    val email : String
)

open class Meeting(
    val name : String,
    val location : Location,
    private val participants : MutableList<Participant>
) {

    open fun addParticipant(participant: Participant) =
            participants.add(participant)

    override fun toString(): String {
        return "Meeting(name='$name', location=$location, participants=$participants)"
    }
}

data class PersonalReview (
    val meetingName: String,
    val meetingLocation: Location,
    val participant: Participant,
    val reviewers: List<Participant>
) : Meeting(meetingName, meetingLocation, mutableListOf<Participant>().apply {
    add(participant)
    addAll(reviewers)
})
{
    init {
        println(successMessage())
    }

    private fun successMessage(): String {
        return "Personal Review for $name created successfully."
    }
}



