package com.example.fitfit.function.pose

import android.util.Log

class SideLunge: Pose() {

    private val TAG = "사이드 런지"

    fun poseSideLunge(outputFeature0: FloatArray): Boolean {

        Log.d(TAG, "poseLunge: 호출됨")

        checkBadPose = ""

        var angleRight = 0.0 // 오른쪽 무릎,엉덩이,발목 각도

        var angleLeft = 0.0 // 왼쪽 무릎,엉덩이,발목 각도

        /**
        왼쪽 골반 = 33
        왼쪽 무릎 = 39
        왼쪽 발목 = 45
         */

        /**
        오른쪽 골반 = 36
        오른쪽 무릎 = 42
        오른쪽 발목 = 48
         */

        // 각도 계산         0.2 = 정확도를 뜻함.
        val leftAccuracy = outputFeature0[35] > 0.2 && outputFeature0[41] > 0.2 && outputFeature0[47] > 0.2
        val rightAccuracy = outputFeature0[38] > 0.2 && outputFeature0[44] > 0.2 && outputFeature0[50] > 0.2

        // 정확도가 0.2 이상일때 감지한다.
        if (leftAccuracy && rightAccuracy) {

            angleLeft = calculateAngle(
                outputFeature0[34], outputFeature0[33], // 왼쪽 골반
                outputFeature0[40], outputFeature0[39], // 왼쪽 무릎
                outputFeature0[46], outputFeature0[45]  // 왼쪽 발목
            )

            angleRight = calculateAngle(
                outputFeature0[37], outputFeature0[36], // 오른쪽 골반
                outputFeature0[43], outputFeature0[42], // 오른쪽 무릎
                outputFeature0[49], outputFeature0[48]  // 오른쪽 발목
            )

        }

        Log.d(TAG, "각도 : 오른쪽 : $angleRight , 왼쪽 : $angleLeft")

        // 앉은 상태 감지
        if (angleRight in 70.0..110.0 && angleLeft in 150.0..180.0 && !sit) {

            Log.d(TAG, "상태: 앉은상태")

            // 잘못된 동작 있는지 확인

            // 잘못된 동작 없을 경우 앉은변수 true
            sit = true

        }

        // 선 상태 감지 및 카운트 증가
        if (angleRight in 150.0..180.0 && angleLeft in 150.0..180.0 && sit) {

            Log.d(TAG, "상태: 앉았다가 선상태")

            sit = false
            stand = false

            return true

        }

        return false

    }

}