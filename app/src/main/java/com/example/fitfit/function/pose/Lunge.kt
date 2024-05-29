package com.example.fitfit.function.pose

import android.util.Log
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

class Lunge() {

    private val TAG = "런지"

    var sit = false
    var stand = false


    // 런지 감지하는 메서드.
    fun poseLunge(outputFeature0: FloatArray): Boolean {

        Log.d(TAG, "poseLunge: 호출됨")

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

        // 각도 계산  0.3 = 정확도를 뜻함.
        val leftAccuracy = outputFeature0[35] > 0.3 && outputFeature0[41] > 0.3 && outputFeature0[47] > 0.3
        val rightAccuracy = outputFeature0[38] > 0.3 && outputFeature0[44] > 0.3 && outputFeature0[50] > 0.3


        // 정확도가 0.3 이상일때 감지한다.
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
        if (angleRight in 70.0..110.0 && angleLeft in 70.0..110.0 && !sit) {

            Log.d(TAG, "상태: 앉은상태")
            sit = true

        }

        // 선 상태 감지 및 카운트 증가
        if (angleRight in 160.0..180.0 && angleLeft in 160.0..180.0 && sit) {

            Log.d(TAG, "상태: 앉았다가 선상태")

            sit = false
            stand = false

            return true

        }

        return false

    } // poseLunge()


    // 세 점의 좌표를 받아서 각도를 계산하는 메서드
    private fun calculateAngle(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        x3: Float,
        y3: Float
    ): Double {

        // 벡터 계산
        val vec1 = Pair(x1 - x2, y1 - y2)
        val vec2 = Pair(x3 - x2, y3 - y2)

        // 벡터의 내적 계산
        val dotProduct = vec1.first * vec2.first + vec1.second * vec2.second

        // 벡터의 크기 계산
        val magnitude1 = sqrt(vec1.first.pow(2) + vec1.second.pow(2))
        val magnitude2 = sqrt(vec2.first.pow(2) + vec2.second.pow(2))

        if (magnitude1 == 0.0f || magnitude2 == 0.0f) {
            return 0.0
        }

        // 코사인 값 계산
        val cosTheta = dotProduct / (magnitude1 * magnitude2)

        // 각도를 라디안으로 계산하고, 도 단위로 변환
        val angleRad = acos(cosTheta)

        return Math.toDegrees(angleRad.toDouble())

    } // calculateAngle

}