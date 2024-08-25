package com.example.fitfit.function.pose


class RightLegRaises: Pose() {

    // 운동 동작 감지하는 메서드
    override fun posePoseExercise(outputFeature0: FloatArray): Boolean {

        checkBadPose = ""

        // 왼쪽 각도
        var leftAngle = 0.0

        // 왼쪽 무릎 = 39
        // 왼쪽 골반 = 33
        // 왼쪽 어깨 = 15
        leftAngle = calculateAngle(
            outputFeature0[40],outputFeature0[39],
            outputFeature0[34],outputFeature0[33],
            outputFeature0[16],outputFeature0[15])


        // 오른쪽 각도
        var rightAngle = 0.0

        // 왼쪽 무릎 = 13 변환 -> 39
        // 오른쪽 골반 = 12 변환 -> 36
        // 오른쪽 무릎 = 14 변환 -> 42
        rightAngle = calculateAngle(
            outputFeature0[40],outputFeature0[39],
            outputFeature0[37],outputFeature0[36],
            outputFeature0[43],outputFeature0[42])

        // 앉은 상태 감지
        if (leftAngle in 150.0..180.0 && rightAngle in 50.0..90.0 && !sit) {

            // 잘못된 동작 있는지 확인
            sit = true

        }

        // 선 상태 감지 및 카운트 증가
        if (leftAngle in 150.0..180.0 && rightAngle in 0.0..30.0 && sit) {

            sit = false
            stand = false
            return true

        }

        return false

    } // posePoseExercise()

}