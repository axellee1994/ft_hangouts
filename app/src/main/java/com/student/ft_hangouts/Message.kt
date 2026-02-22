package com.student.ft_hangouts

data class Message(
    val id: Long = 0,
    val contactId: Long,
    val text: String,
    val timestamp: String,
    val isSent: Boolean
)