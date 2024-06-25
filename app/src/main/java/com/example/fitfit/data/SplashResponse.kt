package com.example.fitfit.data


data class SplashResponse(
   var result: String?,
   var checkList: HashMap<String,Int>?,
   var userAllExerciseList: ArrayList<PoseExercise>?
)