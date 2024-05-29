package com.example.fitfit.function.pose

import android.util.Log
import kotlin.math.acos
import kotlin.math.log
import kotlin.math.pow
import kotlin.math.sqrt

class PushUp {

    private val TAG = "PushUp"

    var isDown = false
    var isUp = false

    fun posePushUp(outputFeature0 : FloatArray) : Boolean{

        Log.d(TAG, "posePushUp: 호출됌")
        var rightElbowBend = 0.0
        var rightKneeAngle = 0.0

        //(x,y,신뢰도) 좌표
        //오른쪽 골반 (36,37,38), 오른쪽 무릎 (42,43,44), 오른쪽 발목 (48,49,50)
        // 오른쪽 어깨 (18,19,20), 오른쪽 팔꿈치 (24,25,26), 오른쪽 손목 (30,31,32)

        /** 오른쪽 팔꿈치 각도 계산 **/
        if (outputFeature0[20] > 0.3 && outputFeature0[26] > 0.3 && outputFeature0[32] > 0.3) {
            rightElbowBend = calculateAngle(
                outputFeature0[19], outputFeature0[18],
                outputFeature0[25], outputFeature0[24],
                outputFeature0[31], outputFeature0[30]
            )
        }

        /** 오른쪽 무릎 각도 계산 **/
        if (outputFeature0[38] > 0.3 && outputFeature0[44] > 0.3 && outputFeature0[50] > 0.3) {
            rightKneeAngle = calculateAngle(
                outputFeature0[37], outputFeature0[36],
                outputFeature0[43], outputFeature0[42],
                outputFeature0[49], outputFeature0[48]
            )
        }

        /**
         * 오른쪽 무릎각도가 160도에서 180도 사이일때 : isKneeBend = false
         * 오른쪽 무릎각도가 160도에서 180도 사이가 아닐때 : isKneeBend = true
         */
        var isKneeBend = rightKneeAngle !in 150.0..180.0

        /**
         * 오른쪽 팔꿈치 각도가 30도에서 80도 사이일때 : isElbowBend = true
         **/
        var isElbowBend = rightElbowBend in 30.0..120.0

        /**
         * 오른쪽 팔꿈치 각도가 160도에서 180도 사이일때 : isElbowExtend = true
         **/
        var isElbowExtend = rightElbowBend in 160.0..180.0


        Log.d(TAG, "isElbowBend : $isElbowBend ")
        Log.d(TAG, "isKneeBend : $isKneeBend")
        /**
         * 다리를 구부리지않고 팔꿈치가 40~60도로 들어왔다가 다시 ready 자세가 되면 true
         */
        if(!isKneeBend) { //무릎은 구부려 지면 안댐

           if(!isDown && !isUp && isElbowBend ){
               isDown = true
               return false
           }

            if(isDown && !isUp && isElbowExtend){
                isUp = true
                return false
            }

           if(isDown && isUp){
               isDown = false
               isUp = false
               return true
           }

        }else{ //구부려지면 다시 초기화
            isDown = false
            isUp = false
            return false
        }

        return false
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

    } // calculateAngle(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Double

}
