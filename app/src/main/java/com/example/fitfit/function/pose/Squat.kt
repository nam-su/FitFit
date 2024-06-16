package com.example.fitfit.function.pose

import android.util.Log
import kotlinx.coroutines.coroutineScope
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

class Squat() {

    private val TAG = "Squat"

    var sit = false
    var stand = false

    // 스쿼트 자세에서 허리 불량을 판별하는 변수
    var checkBadPose = ""


    // 스쿼트 동작인식 메서드
    fun poseSquat(outputFeature0 : FloatArray) : Boolean{

        Log.d(TAG, "poseSquat: 호출됌")

        checkBadPose = ""

        // 각도 계산
        val angle = if (outputFeature0[35] > 0.3 && outputFeature0[41] > 0.3 && outputFeature0[47] > 0.3) {
            calculateAngle(
                outputFeature0[34], outputFeature0[33],
                outputFeature0[40], outputFeature0[39],
                outputFeature0[46], outputFeature0[45]
            )
        } else {

            0.0

        }

        Log.d(TAG, "앉았을때 발목,무릎,골반 각도 : $angle")

        // 앉은 상태 감지
        if (angle in 70.0..110.0 && !sit) {

            Log.d(TAG, "상태: 앉은 상태")

            if (checkWaistAngle(outputFeature0)) {

                Log.d(TAG, "올바른 앉은 자세")
                sit = true

            } else {

                Log.d(TAG, "허리 자세 불량")
                checkBadPose = "허리를 곧게 펴세요"

            }

        }

        // 선 상태 감지 및 카운트 증가
        if (angle in 150.0..180.0 && sit) {

            Log.d(TAG, "상태: 앉았다가 선상태")
            sit = false
            stand = false
            return true

        }

        return false
    }


    // 앉았을 때 허리 각도가 잘못된지 판단여부 메서드
    private fun checkWaistAngle(outputFeature0: FloatArray): Boolean {


        // 왼쪽 무릎 = 13 변환값 = 39
        // 왼쪽 골반 = 11 변환값 = 33
        // 왼쪽 어깨 = 5 변환값 = 15
        val angleWaist = calculateAngle(
            outputFeature0[40],outputFeature0[39],
            outputFeature0[34],outputFeature0[33],
            outputFeature0[16],outputFeature0[15])

        Log.d(TAG, "poseSquat: 앉았을때 무릎,골반,어깨 각도 : $angleWaist")

        return angleWaist in 80.0..110.0

    } // checkWaistAngle()


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

    } // calculateAngel()

}