package com.example.backgroundservice.Model.Live

data class LivematchItem(
    val events: List<Event>,
    val fixture: Fixture,
    val goals: Goals,
    val league: League,
    val score: Score,
    val teams: Teams
)