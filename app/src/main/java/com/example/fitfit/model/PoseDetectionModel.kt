package com.example.fitfit.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication
import com.example.fitfit.function.pose.Lunge
import com.example.fitfit.function.pose.PushUp
import com.example.fitfit.function.pose.Squat
import com.example.fitfit.ml.AutoModel4
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PoseDetectionModel(context: Context) {

    private val TAG = "포즈 추정 모델"

    // 레트로핏 관련 초기화
    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

    // 이미지 처리를 위한 ImageProcessor 초기화
    private val imageProcessor: ImageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(192, 192, ResizeOp.ResizeMethod.BILINEAR))
        .build()

    // ML 모델 초기화
    private val model: AutoModel4 = AutoModel4.newInstance(context)

    // 그리기 위한 Paint 객체 초기화
    private val paint = Paint().apply { color = Color.YELLOW }

    private val pushUp = PushUp()
    private val lunge = Lunge()
    private val squat = Squat()

    var count = 0

    // 이미지를 처리하고, 결과 비트맵과 카운트를 반환하는 메서드
    fun processImage(bitmap: Bitmap,exerciseName: String): Pair<Bitmap, Int> {

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

        // 운동 종류에 따른 운동 판별하는 메서드
        poseExercise(exerciseName,outputFeature0)

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


    // 운동 종류에 따른 카운트 변화
    private fun poseExercise(exerciseName: String,floatArray: FloatArray) {

        when(exerciseName) {

            "기본 스쿼트" -> if(squat.poseSquat(floatArray)){count ++}
            "기본 푸시업" -> if(pushUp.posePushUp(floatArray)){count ++}
            "기본 런지" -> if(lunge.poseLunge(floatArray)){count ++}

        }

    } // poseExercise()


    // 쉐어드에 운동 후 기록 저장하는 메서드.
    suspend fun updatePoseExercise(exerciseName: String): Response<PoseExercise> {

        // 쉐어드로 운동객체 호출 하고 , 그 객체 갱신 후 리스트 갱신 해줘야함.
        val poseExercise = MyApplication.sharedPreferences.getPoseExercise(exerciseName)

        // 유저 id 값을 불러와서 서버와 통신.
        val id = MyApplication.sharedPreferences.getUserId()

        // 최근 운동 날짜와 방금 운동한 날짜가 같을 떄는 update()
        if(isSameDate(poseExercise)) {

            // 서버로 update 요청.
            return retrofitInterface.updatePoseExercise(

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
            return retrofitInterface.insertPoseExercise(

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

        // 여기서 현재 날짜와 그전 데이터 날짜 값 시스템 시간에서 현재 날짜 형식으로 변환
        val beforeDate: String = LocalDate.ofEpochDay(poseExercise.date / (24 * 60 * 60 * 1000))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val afterDate: String = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val checkDate: Boolean

        // 날짜가 같은 경우
        if(beforeDate == afterDate) {

            // 운동 카운트,시스템 시간 초기화
            poseExercise.exerciseCount += count
            poseExercise.date = System.currentTimeMillis()
            checkDate = true

        // 날짜가 다른 경우
        } else {

            // 운동 카운트 초기화 , 시스템 시간 초기화
            poseExercise.exerciseCount = count
            poseExercise.date = System.currentTimeMillis()
            checkDate = false

        }

        // 운동 객체 초기화 해줌.
        MyApplication.sharedPreferences.setPoseExercise(poseExercise)

        // 쉐어드 운동 리스트 갱신해준다.
        MyApplication.sharedPreferences.updatePoseExerciseList(poseExercise)

        return checkDate

    } // compareDate()


    // 모델을 닫는 메서드
    fun close() {
        model.close()
    } // close()
}

