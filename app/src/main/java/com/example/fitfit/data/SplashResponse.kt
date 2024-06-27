package com.example.fitfit.data


data class SplashResponse(
   var result: String?,
   var checkList: String?,
   var userAllExerciseList: ArrayList<PoseExercise>?
)