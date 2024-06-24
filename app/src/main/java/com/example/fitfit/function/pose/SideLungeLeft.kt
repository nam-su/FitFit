package com.example.fitfit.function.pose

import android.util.Log

class SideLungeLeft : Pose() {

    private val TAG = "왼쪽 런지"

    override fun posePoseExercise(outputFeature0: FloatArray): Boolean {

        Log.d(TAG, "posePoseExercise:")

        checkBadPose = ""

        var angleRight = 0.0 // 오른쪽 무릎,엉덩이,발목 각도

        var angleLeft = 0.0 // 왼쪽 무릎,엉덩이,발목 각도

        /**
        왼쪽 발목 = 45
        왼쪽 무릎 = 39
        왼쪽 골반 = 33
         */

        /**
        오른쪽 발목 = 48
        오른쪽 무릎 = 42
        오른쪽 골반 = 36
         */


        // 정확도가 0.2 이상일때 감지한다.

        angleLeft = calculateAngle(
            outputFeature0[46], outputFeature0[45], // 왼쪽 발목
            outputFeature0[40], outputFeature0[39], // 왼쪽 무릎
            outputFeature0[34], outputFeature0[33]  // 왼쪽 골반
        )

        angleRight = calculateAngle(
            outputFeature0[49], outputFeature0[48], // 오른쪽 발목
            outputFeature0[43], outputFeature0[42], // 오른쪽 무릎
            outputFeature0[37], outputFeature0[36]  // 오른쪽 골반
        )


        Log.d(TAG, "각도 : 오른쪽 : $angleRight , 왼쪽 : $angleLeft")

        // 오른쪽 다리 각도 115 155
        // 앉은 상태 감지
        if (angleRight in 160.0..180.0 && angleLeft in 80.0..120.0 && !sit) {

            Log.d(TAG, "상태: 앉은상태")

            // 잘못된 동작 없을 경우 앉은변수 true
            sit = true

        }

        // 선 상태 감지 및 카운트 증가
        if (angleRight in 160.0..180.0 && angleLeft in 150.0..180.0 && sit) {

            Log.d(TAG, "상태: 앉았다가 선상태")

            sit = false
            stand = false

            return true

        }

        return false

    }

}