package com.example.footballnotification.SingleMatch

data class SingleMatchDetailsItem(
    val events: List<Event>,
    val fixture: Fixture,
    val goals: Goals,
    val league: League,
    val lineups: List<Lineup>,
    val players: List<PlayerXXXX>,
    val score: Score,
    val statistics: List<StatisticX>,
    val teams: Teams
)