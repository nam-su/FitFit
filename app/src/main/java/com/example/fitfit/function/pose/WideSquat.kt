package com.example.fitfit.function.pose

import android.util.Log

class WideSquat: Pose(){

    private val TAG = "와이드스쿼트"


    // 운동 동작 감지하는 메서드
    override fun posePoseExercise(outputFeature0: FloatArray): Boolean {

        checkBadPose = ""

        var leftAngle = 0.0
        var rightAngle = 0.0

        val leftReliability = outputFeature0[41] > 0.2 && outputFeature0[35] > 0.2 && outputFeature0[38] > 0.2
        val rightReliability = outputFeature0[44] > 0.2 && outputFeature0[38] > 0.2 && outputFeature0[35] > 0.2

        // 신뢰도가 0.2 이상일 때 각도 계산
        if (leftReliability && rightReliability) {

            // 구해야 하는것 1번 왼쪽 무릎(13) 왼쪽 골반(11) 오른쪽 골반의 각도(12)
            leftAngle = calculateAngle(
                outputFeature0[40],outputFeature0[39],
                outputFeature0[34],outputFeature0[33],
                outputFeature0[37],outputFeature0[36])

            // 구해야 하는것 2번 오른쪽 무릎(14) 오른쪽 골반(12) 왼쪽 골반(11)
            rightAngle = calculateAngle(
                outputFeature0[43],outputFeature0[42],
                outputFeature0[37],outputFeature0[36],
                outputFeature0[34],outputFeature0[33])

        }

        Log.d(TAG, "poseWideSquat: 왼쪽 각도 : $leftAngle")
        Log.d(TAG, "poseWideSquat: 오른쪽 각도 : $rightAngle")
        
        // 앉은 상태 감지 150 ~ 180 사이로 나옴
        if(rightAngle in 140.0..180.0 && leftAngle in 140.0..180.0 && !sit) {

            Log.d(TAG, "poseWideSquat: 앉은 상태")
            sit = true

        }

        // 일어섬 감지 100 정도 나옴
//        // 1번 서있을 때 다리를 벌려야 한다 이 각도는 계속 가져가야함
        if(leftAngle in 80.0..120.0 && rightAngle in 80.0..120.0 && sit) {

            Log.d(TAG, "poseWideSquat: 앉았다가 선상태")
            sit = false
            stand = false
            return true

        }

        return false

    } // posePoseExercise()

}