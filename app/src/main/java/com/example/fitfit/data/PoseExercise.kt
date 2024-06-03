package com.example.fitfit.data

import com.google.gson.annotations.SerializedName

class PoseExercise(
    @SerializedName("date") var date: Long,
    @SerializedName("category") val category: String,
    @SerializedName("exerciseName") val exerciseName: String,
    @SerializedName("exerciseCount") var exerciseCount: Int,
    @SerializedName("goalExerciseCount") val goalExerciseCount: Int) {

    @SerializedName("result") var result: String? = null
    @SerializedName("checkList") var checkList: Boolean? = false

}