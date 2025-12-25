package com.student.ft_hangouts

// Contact box to hold data. Long = 0 for new contacts
data class Contact (
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val info: String
)