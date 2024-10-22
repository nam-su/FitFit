package com.example.fitfit.function.pose

class LeftLegRaises: Pose() {

    // 운동 동작 인식 메서드
    override fun posePoseExercise(outputFeature0: FloatArray): Boolean {

        checkBadPose = ""

        // 왼쪽 각도
        var leftAngle = 0.0

        // 오른쪽 무릎 = 42
        // 왼쪽 골반 = 33
        // 왼쪽 무릎 = 39
        leftAngle = calculateAngle(
            outputFeature0[43],outputFeature0[42],
            outputFeature0[34],outputFeature0[33],
            outputFeature0[40],outputFeature0[39])

        // 오른쪽 각도
        var rightAngle = 0.0

        // 오른쪽 발목 -> 48
        // 오른쪽 무릎 -> 42
        // 오른쪽 골반 -> 18
        rightAngle = calculateAngle(
            outputFeature0[49],outputFeature0[48],
            outputFeature0[43],outputFeature0[42],
            outputFeature0[19],outputFeature0[18])
        
        // 앉은 상태 감지
        if (leftAngle in 50.0..90.0 && rightAngle in 150.0..180.0 && !sit) {

            // 잘못된 동작 있는지 확인
            sit = true

        }

        // 선 상태 감지 및 카운트 증가
        if (leftAngle in 0.0..30.0 && rightAngle in 150.0..180.0 && sit) {

            sit = false
            stand = false
            return true

        }

        return false

    } // posePoseExercise()

}