package com.example.fitfit.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication
import com.example.fitfit.function.pose.LeftLegRaises
import com.example.fitfit.function.pose.LegRaises
import com.example.fitfit.function.pose.Lunge
import com.example.fitfit.function.pose.Pose
import com.example.fitfit.function.pose.PushUp
import com.example.fitfit.function.pose.RightLegRaises
import com.example.fitfit.function.pose.SideLungeLeft
import com.example.fitfit.function.pose.SideLungeRight
import com.example.fitfit.function.pose.Squat
import com.example.fitfit.function.pose.WideSquat
import com.example.fitfit.ml.AutoModel4
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import retrofit2.Response
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PoseDetectionModel(context: Context,var exerciseName: String) {

    private val TAG = "포즈 추정 모델"

    // 이미지 처리를 위한 ImageProcessor 초기화
    private val imageProcessor: ImageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(192, 192, ResizeOp.ResizeMethod.BILINEAR))
        .build()

    // ML 모델 초기화
    private val model: AutoModel4 = AutoModel4.newInstance(context)

    // 그리기 위한 Paint 객체 초기화
    private val paint = Paint().apply { color = Color.YELLOW }

    private lateinit var pose: Pose

    // 운동 카운트를 측정하기 위한 전역 변수
    var count = 0

    // 자세에서 필요한 정확도 검사 하는 변수
    var checkAccuracy: Boolean = false

    // 자세불량을 판별하는 변수
    var checkBadPose = ""

    init {

        // 운동 이름에 따른 객체 초기화 진행
        when(exerciseName) {

            "기본 스쿼트" -> pose = Squat()

            "기본 푸시업" -> pose = PushUp()

            "기본 런지" -> pose = Lunge()

            "와이드 스쿼트" -> pose = WideSquat()

            "왼쪽 런지" -> pose = SideLungeLeft()

            "오른쪽 런지" -> pose = SideLungeRight()

            "기본 레그레이즈" -> pose = LegRaises()

            "왼쪽 레그레이즈" -> pose = LeftLegRaises()

            "오른쪽 레그레이즈" -> pose = RightLegRaises()

        }

    }


    // 운동 시작 자세 안내해주는 메서드
    fun startPoseExercise(exerciseName: String): String {

        return when(exerciseName) {

            "기본 스쿼트" -> "기본 스쿼트 시작합니다. 양발의 간격은 어깨넓이로 벌리고 카메라를 기준으로 45도 오른쪽을 보고 서주세요."

            "기본 푸시업" -> "기본 푸시업 시작합니다. 팔을 어깨너비보다 약간 넓게 벌린 상태로 카메라를 기준으로 45도 오른쪽을 보고 엎드려 주세요."

            "기본 런지" -> "기본 런지 시작합니다. 두 발을 골반 너비로 벌린 후 카메라 기준으로 왼쪽을 보고 서주세요."

            "와이드 스쿼트" -> "와이드 스쿼트 시작합니다. 다리를 어깨너비 보다 넓게 벌리고 양쪽 발끝은 밖으로 향한 후 카메라를 보고 정면으로 서주세요."

            "왼쪽 런지" -> "왼쪽 런지 시작합니다. 두 발을 골반너비로 벌린 자세로 카메라 기준으로 왼쪽을 보고 서주세요."

            "오른쪽 런지" -> "오른쪽 런지 시작합니다. 두 발을 골반너비로 벌린 자세로 카메라 기준으로 왼쪽을 보고 서주세요."

            "기본 레그레이즈" -> "기본 레그레이즈 시작합니다. 손바닥을 지면에 대고 바른 자세로 카메라 기준 왼쪽으로 누워 주세요."

            "왼쪽 레그레이즈" -> "왼쪽 레그레이즈 시작합니다. 손에 머리를 기대고 시선을 옆방향에 두고 카메라 기준 왼쪽으로 누워 주세요."

            "오른쪽 레그레이즈" -> "오른쪽 레그레이즈 시작합니다. 손에 머리를 기대고 시선을 옆방향에 두고 카메라 기준 왼쪽으로 누워 주세요."

            else -> "잘못된 운동 입니다."

        }

    } // startPoseExercise()


    // 이미지를 처리하고, 결과 비트맵과 카운트를 반환하는 메서드
    fun processImage(bitmap: Bitmap): Pair<Bitmap, Int> {

        // 비트맵을 TensorImage 객체에 로드
        var tensorImage = TensorImage(DataType.UINT8)
        tensorImage.load(bitmap)
        tensorImage = imageProcessor.process(tensorImage)

        // 모델 입력 준비
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 192, 192, 3), DataType.UINT8)
        inputFeature0.loadBuffer(tensorImage.buffer)

        // 모델 추론 실행
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

        // 모델 출력을 기반으로 비트맵을 수정할 준비
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val h = bitmap.height
        val w = bitmap.width

        var x = 0

        // 자세에서 필요한 정확도 검사 하는 변수
        checkAccuracy = if (checkExerciseAccuracy(outputFeature0)) {

            // 운동 종류에 따른 운동 판별하는 메서드
            poseExercise(outputFeature0)
            true

        } else {

            false

        }

        // 추론된 점들을 그리기
        while (x <= 49) {

            // 신뢰도가 0.45 이상인 점들만 처리
            if (outputFeature0[x + 2] > 0.45) {

                // 캔버스에 점 그리기
                canvas.drawCircle(outputFeature0[x + 1] * w, outputFeature0[x] * h, 10f, paint)

            }

            x += 3

        } // 점그리기 메서드 끝.

        return Pair(mutableBitmap, count)

    } // processImage()


    // 운동 할때 정확도 측정
    private fun checkExerciseAccuracy(outputFeature0: FloatArray): Boolean {

        val accuracyThreshold = 0.1

        return listOf(

            outputFeature0[17], outputFeature0[20], outputFeature0[23], outputFeature0[26],
            outputFeature0[29], outputFeature0[32], outputFeature0[35], outputFeature0[38],
            outputFeature0[41], outputFeature0[44], outputFeature0[47], outputFeature0[50]

        ).all { it > accuracyThreshold }

    } // checkExerciseAccuracy()


    // 운동 종류에 따른 카운트 변화
    private fun poseExercise(floatArray: FloatArray) {

        if(pose.posePoseExercise(floatArray)) {

            count ++

        } else {

            checkBadPose = pose.checkBadPose

        }

    } // poseExercise()


    // 운동 후 기록 저장하는 메서드.
    suspend fun updatePoseExercise(exerciseName: String): Response<PoseExercise> {

        // 레트로핏 관련 초기화
        val retrofitBuilder = RetrofitBuilder()
        val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

        // 쉐어드로 운동객체 호출 하고 , 그 객체 갱신 후 리스트 갱신 해줘야함.
        val poseExercise = MyApplication.sharedPreferences.getPoseExercise(exerciseName)

        MyApplication.sharedPreferences.exceptionNoDataMyAllExercise(poseExercise)

        // 유저 id 값을 불러와서 서버와 통신.
        val id = MyApplication.sharedPreferences.getUserId()

        // 최근 운동 날짜와 방금 운동한 날짜가 같을 떄는 update()
        return if(isSameDate(poseExercise)) {

            // 서버로 update 요청.
            retrofitInterface.updatePoseExercise(

                id,
                poseExercise.category,
                poseExercise.exerciseName,
                poseExercise.exerciseCount,
                poseExercise.goalExerciseCount,
                poseExercise.date,
                poseExercise.checkList,
                "updateUserExercise")

        // 날짜가 다른 경우에 insert 해준다.
        } else {

            // 서버로 insert 요청.
            retrofitInterface.insertPoseExercise(

                id,
                poseExercise.category,
                poseExercise.exerciseName,
                poseExercise.exerciseCount,
                poseExercise.goalExerciseCount,
                poseExercise.date,
                poseExercise.checkList,
                "insertUserExercise")

        }

    } // updatePoseExercise()


    // 날짜 비교해서 쉐어드 다르게 들어가야하는 메서드
    private fun isSameDate(poseExercise: PoseExercise): Boolean {

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val zoneId = ZoneId.of("Asia/Seoul")
        val date = Instant.ofEpochMilli(poseExercise.date).atZone(zoneId).toLocalDate()

        val previousDate = date.format(formatter)
        val currentDate = LocalDate.now().format(formatter)
        Log.d(TAG, "서버에 저장되있는 날짜: $previousDate")
        Log.d(TAG, "오늘날짜: $currentDate")

        // 이전 날짜와 현재 날짜가 같은 경우. 카운트 + 해준다
        return if (previousDate == currentDate) {

            Log.d(TAG, "isSameDate: same")

            poseExercise.exerciseCount += count
            poseExercise.date = System.currentTimeMillis()
            MyApplication.sharedPreferences.updateMyPoseExerciseList(poseExercise)
            true

            // 이전 날짜와 현재 날짜가 다른 경우 방금한 운동이 카운트로 초기화됨.
        } else {

            Log.d(TAG, "isSameDate: differ")


            poseExercise.exerciseCount = count
            poseExercise.date = System.currentTimeMillis()
            MyApplication.sharedPreferences.updateMyPoseExerciseList(poseExercise)
            false

        }

    } // compareDate()


    // 모델을 닫는 메서드
    fun close() = model.close()
    // close()

}

