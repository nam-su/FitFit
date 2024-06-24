package com.example.fitfit.function.pose

import android.util.Log
import kotlin.math.acos
import kotlin.math.log
import kotlin.math.pow
import kotlin.math.sqrt

class PushUp: Pose() {

    private val TAG = "PushUp"


    override fun posePoseExercise(outputFeature0 : FloatArray) : Boolean{

        checkBadPose = ""

        Log.d(TAG, "posePushUp: 호출됌")
        var rightElbowBend = 0.0
        var rightKneeAngle = 0.0

        //(x,y,신뢰도) 좌표
        //오른쪽 골반 (36,37,38), 오른쪽 무릎 (42,43,44), 오른쪽 발목 (48,49,50)
        // 오른쪽 어깨 (18,19,20), 오른쪽 팔꿈치 (24,25,26), 오른쪽 손목 (30,31,32)

        /** 오른쪽 팔꿈치 각도 계산 **/
        if (outputFeature0[20] > 0.2 && outputFeature0[26] > 0.2 && outputFeature0[32] > 0.2) {

            rightElbowBend = calculateAngle(
                outputFeature0[19], outputFeature0[18],
                outputFeature0[25], outputFeature0[24],
                outputFeature0[31], outputFeature0[30]
            )

        }

        /** 오른쪽 무릎 각도 계산 **/
        if (outputFeature0[38] > 0.2 && outputFeature0[44] > 0.2 && outputFeature0[50] > 0.2) {

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
        val isKneeBend = rightKneeAngle !in 150.0..180.0

        /**
         * 오른쪽 팔꿈치 각도가 30도에서 80도 사이일때 : isElbowBend = true
         **/
        val isElbowBend = rightElbowBend in 30.0..110.0

        /**
         * 오른쪽 팔꿈치 각도가 160도에서 180도 사이일때 : isElbowExtend = true
         **/
        val isElbowExtend = rightElbowBend in 150.0..180.0


        Log.d(TAG, "isElbowBend : $isElbowBend ")
        Log.d(TAG, "isKneeBend : $isKneeBend")
        /**
         * 다리를 구부리지않고 팔꿈치가 40~60도로 들어왔다가 다시 ready 자세가 되면 true
         */


        if(!isKneeBend) { //무릎은 구부려 지면 안댐

           // down
           if(!sit && !stand && isElbowBend ){

               sit = true
               return false

           }

           // up
            if(sit && !stand && isElbowExtend){
                stand = true
                return false

            }

           // 두개조건 만족 했을 때
           if(sit && stand){
               sit = false
               stand = false
               return true
           }

        }else{ //구부려지면 다시 초기화
            
            if (rightKneeAngle != 0.0) {

                Log.d(TAG, "posePushUp: $rightKneeAngle")
                checkBadPose = "무릎을 일자로 펴주세요"

            }

            sit = false
            stand = false
            return false

        }

        return false
    }

}
