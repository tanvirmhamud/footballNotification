package com.example.footballnotification.SingleMatch

data class Event(
    val assist: Assist,
    val comments: Any,
    val detail: String,
    val player: Player,
    val team: Team,
    val time: Time,
    val type: String
)