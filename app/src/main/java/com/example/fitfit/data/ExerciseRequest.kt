package com.example.fitfit.data

data class ExerciseRequest(
    val id: String,
    val myExerciseList: HashMap<String,Int>,
    val mode: String
)