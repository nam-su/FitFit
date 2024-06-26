package com.example.fitfit.function.pose

import android.util.Log
import kotlinx.coroutines.coroutineScope
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

class Squat(): Pose() {

    private val TAG = "Squat"


    // 스쿼트 동작인식 메서드
    override fun posePoseExercise(outputFeature0 : FloatArray) : Boolean{

        Log.d(TAG, "poseSquat: 호출됌")

        checkBadPose = ""

        var angle = 0.0

        // 각도 계산
        if (outputFeature0[35] > 0.2 && outputFeature0[41] > 0.2 && outputFeature0[47] > 0.2) {

            angle = calculateAngle(
                outputFeature0[34], outputFeature0[33],
                outputFeature0[40], outputFeature0[39],
                outputFeature0[46], outputFeature0[45]
            )

        }

        // 앉은 상태 감지
        if (angle in 55.0..110.0 && !sit) {

            Log.d(TAG, "상태: 앉은 상태")

            // 잘못된 동작 있는지 확인
            if(checkBadPose(outputFeature0)) {

                // 잘못된 동작 없을 경우 앉은변수 true
                sit = true

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


    // 앉았을 자세가 잘못된지 판단여부 메서드
    private fun checkBadPose(outputFeature0: FloatArray): Boolean {

        // 허리 관련

        // 왼쪽 무릎 = 13 변환값 = 39
        // 왼쪽 골반 = 11 변환값 = 33
        // 왼쪽 어깨 = 5 변환값 = 15
        val angleWaist = calculateAngle(
            outputFeature0[40],outputFeature0[39],
            outputFeature0[34],outputFeature0[33],
            outputFeature0[16],outputFeature0[15])

        checkBadPose = "허리를 곧게 펴세요"

        return angleWaist in 80.0..110.0

    } // checkBadPose()

}