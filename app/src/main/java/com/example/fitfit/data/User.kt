package com.example.fitfit.data

import com.example.fitfit.function.pose.Pose
import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("id") val id: String,
        @SerializedName("loginType") val loginType: String,
        @SerializedName("nickname")  val nickname: String,
        @SerializedName("profileImagePath") val profileImagePath: String,
        @SerializedName("subscription") val subscription: String
        )

      {

          @SerializedName("result") var result: String? = null
          @SerializedName("checkList") var checkList: String? = null
          @SerializedName("userAllExerciseList") var userAllExerciseList: ArrayList<PoseExercise>? = null
          @SerializedName("challengeList") var challengeList: ArrayList<Challenge>? = null
          @SerializedName("myChallenge") var myChallengeList: ArrayList<Challenge>? = null

      }