package com.problemsolver.event.data.model

data class AllEvent(
    val upcoming: List<Event>,
    val finished: List<Event>
)
