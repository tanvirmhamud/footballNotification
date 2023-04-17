package com.example.footballnotification.SingleMatch

data class Statistic(
    val cards: Cards,
    val dribbles: Dribbles,
    val duels: Duels,
    val fouls: Fouls,
    val games: Games,
    val goals: GoalsX,
    val offsides: Any,
    val passes: Passes,
    val penalty: Penalty,
    val shots: Shots,
    val tackles: Tackles
)