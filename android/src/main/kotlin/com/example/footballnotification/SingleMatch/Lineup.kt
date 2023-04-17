package com.example.footballnotification.SingleMatch

data class Lineup(
    val coach: Coach,
    val formation: String,
    val startXI: List<StartXI>,
    val substitutes: List<Substitute>,
    val team: TeamX
)