package com.example.fitfit.function.pose

import android.util.Log

class LegRaises : Pose() {

    val TAG = "레그레이즈"


    // 운동 동작 인식 메서드
    override fun posePoseExercise(outputFeature0: FloatArray): Boolean {

        checkBadPose = ""

        // 왼쪽 각도
        var leftAngle = 0.0

        // 왼쪽 무릎 = 13 변환 -> 39
        // 왼쪽 골반 = 11 변환 -> 33
        // 왼쪽 어깨 = 5 변환 -> 15
        leftAngle = calculateAngle(
            outputFeature0[40],outputFeature0[39],
            outputFeature0[34],outputFeature0[33],
            outputFeature0[16],outputFeature0[15])

        // 오른쪽 각도
        var rightAngle = 0.0

        // 오른쪽 무릎 = 14 변환 -> 42
        // 오른쪽 골반 = 12 변환 -> 36
        // 오른쪽 어깨 = 6 변환 -> 18
        rightAngle = calculateAngle(
            outputFeature0[43],outputFeature0[42],
            outputFeature0[37],outputFeature0[36],
            outputFeature0[19],outputFeature0[18])

        // 앉은 상태 감지
        if (leftAngle in 80.0..110.0 && rightAngle in 80.0..110.0 && !sit) {

            // 잘못된 동작 있는지 확인
            sit = true

        }

        // 선 상태 감지 및 카운트 증가
        if (rightAngle in 150.0..180.0 && leftAngle in 150.0..180.0 && sit) {

            Log.d(TAG, "상태: 앉았다가 선상태")
            sit = false
            stand = false
            return true

        }

        return false

    } // posePoseExercise()

}