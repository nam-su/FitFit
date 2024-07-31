package com.example.fitfit.data

import com.google.gson.annotations.SerializedName

data class Rank(

    val ranking: Int,
    val id: String,
    val nickname: String,
    val profileImagePath: String,
    val challengeName: String,
    val rankingPoint: Int,
    val standard: String)

