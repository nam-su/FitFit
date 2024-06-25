package com.example.fitfit.data

data class ExerciseRequest(
    val id: String,
    val myExerciseList: ArrayList<PoseExercise>,
    val mode: String
)