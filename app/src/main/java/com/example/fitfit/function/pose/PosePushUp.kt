package com.example.fitfit.function.pose

import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

class PosePushUp(outputFeature0 : FloatArray) {

    private fun workout(){

    }
    // 세 점의 좌표를 받아서 각도를 계산하는 메서드
    private fun calculateAngle(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Double {

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
    }

}
