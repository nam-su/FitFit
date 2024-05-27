package com.example.fitfit.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.example.fitfit.function.pose.PosePushUp
import com.example.fitfit.ml.AutoModel4
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

class PoseDetectionModel(context: Context) {

    // 이미지 처리를 위한 ImageProcessor 초기화
    private val imageProcessor: ImageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(192, 192, ResizeOp.ResizeMethod.BILINEAR))
        .build()

    // ML 모델 초기화
    private val model: AutoModel4 = AutoModel4.newInstance(context)

    // 그리기 위한 Paint 객체 초기화
    private val paint = Paint().apply { color = Color.YELLOW }

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
        var angle: Double?
        var count = 0

        Log.d("카운트", "onSurfaceTextureUpdated: $count")

        // 운동 상태 플래그 초기화
        var stand = false
        var sit = false


        // 찍히는 point 17 개 배열 51 개 배열안에 한 인덱스가 3개씩 차지.
        // 엉덩이 포인트 : 왼쪽 궁둥이 11 오른쪽 궁둥이 12 환산하면 33 , 36
        // 무릎 포인트 : 왼쪽 13 오른쪽 14 환산하면 39 , 42
        // 어깨 포인트 : 왼쪽 5 오른쪽 6 환산하면 15 , 18

        // 왼쪽 발목 = x == 45
        // 왼쪽 어깨 = X == 15일때
        // 왼쪽 엉덩이 = X == 33 일때
        // 왼쪽 무릎 = X == 39 일때


        // 추론된 점들을 그리기
        while (x <= 49) {
            // 신뢰도가 0.45 이상인 점들만 처리
            if (outputFeature0[x + 2] > 0.45) {
                if (stand && sit) {
                    stand = false
                    sit = false
                }

                // 캔버스에 점 그리기
                canvas.drawCircle(outputFeature0[x + 1] * w, outputFeature0[x] * h, 10f, paint)

                // 각도 계산
                angle = calculateAngle(
                    outputFeature0[34] * w, outputFeature0[33] * h,
                    outputFeature0[40] * w, outputFeature0[39] * h,
                    outputFeature0[46] * w, outputFeature0[45] * h
                )

                // 앉은 상태 감지
                if (angle!! in 70.0..120.0) {
                    sit = true
                }

                // 선 상태 감지 및 카운트 증가
                if (angle in 160.0..180.0 && sit) {
                    stand = true
                    count++
                }
            }
            x += 3
        }

        return Pair(mutableBitmap, count)
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

    // 모델을 닫는 메서드
    fun close() {
        model.close()
    }
}

