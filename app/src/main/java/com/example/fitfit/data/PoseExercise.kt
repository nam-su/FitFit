package com.example.fitfit.data

import com.google.gson.annotations.SerializedName

data class PoseExercise(
    @SerializedName("date") var date: Long,
    @SerializedName("category") val category: String,
    @SerializedName("exerciseName") val exerciseName: String,
    @SerializedName("exerciseCount") var exerciseCount: Int,
    @SerializedName("goalExerciseCount") val goalExerciseCount: Int,
    @SerializedName("checkList") var checkList: Int) {

    @SerializedName("result") var result: String? = null
    @SerializedName("primium") var isPrimium: Int = 0



}