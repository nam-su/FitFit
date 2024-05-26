package com.example.fitfit.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import com.example.fitfit.R
import com.example.fitfit.databinding.FragmentPoseDetectionBinding
import com.example.fitfit.ml.AutoModel4
import com.example.fitfit.viewModel.PoseDetectionViewModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

class PoseDetectionFragment : Fragment() {


    private lateinit var binding: FragmentPoseDetectionBinding
    private lateinit var poseDetectionViewModel: PoseDetectionViewModel

    // Sam의 테스트 변수
    val sam = "샘의 테스트 변수"

    // 원 그리기 위한 Paint 객체
    val paint = Paint()

    // 이미지 처리를 위한 구성요소
    lateinit var imageProcessor: ImageProcessor
    lateinit var model: AutoModel4

    // 캡처된 이미지를 보유할 Bitmap
    lateinit var bitmap: Bitmap

    // 카메라 관련 구성요소
    lateinit var cameraManager: CameraManager
    lateinit var handler: Handler
    lateinit var handlerThread: HandlerThread


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pose_detection, container, false)

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 운동 카운트 관련 변수
        var stand = false
        var sit = false
        var count = 0

        // 필요한 권한 요청
        get_permissions()

        // 이미지 처리 설정
        imageProcessor = ImageProcessor
            .Builder()
            .add(ResizeOp(192,192, ResizeOp.ResizeMethod.BILINEAR)).build()

        model = AutoModel4.newInstance(requireContext())

        // 뷰 및 카메라 구성요소 초기화
        cameraManager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)


        // 원 그리기 위한 Paint 색상 설정
        paint.color = Color.YELLOW

        // TextureView 리스너 설정하여 카메라 미리보기 처리
        binding.textureView.surfaceTextureListener = object: TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                // TextureView가 사용 가능할 때 카메라 열기
                open_camera()
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                // TextureView 크기 변경 처리
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                // TextureView가 업데이트될 때 캡처된 이미지 처리

                // TextureView에서 비트맵 가져오기
                bitmap = binding.textureView.bitmap!!

                // 비트맵을 TensorImage에 로드
                var tensorImage = TensorImage(DataType.UINT8)
                tensorImage.load(bitmap)
                tensorImage = imageProcessor.process(tensorImage)

                // 모델 추론을 위한 입력 준비
                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 192, 192, 3), DataType.UINT8)
                inputFeature0.loadBuffer(tensorImage.buffer)

                // 모델 추론 실행
                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

                // 모델 출력을 기반으로 비트맵에 그리기
                var mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                var canvas = Canvas(mutable)

                var h = bitmap.height
                var w = bitmap.width

                var x = 0
                var angle: Double? = 0.0

                Log.d("카운트", "onSurfaceTextureUpdated: $count")

                // 찍히는 point 17 개 배열 51 개 배열안에 한 인덱스가 3개씩 차지.
                // 엉덩이 포인트 : 왼쪽 궁둥이 11 오른쪽 궁둥이 12 환산하면 33 , 36
                // 무릎 포인트 : 왼쪽 13 오른쪽 14 환산하면 39 , 42
                // 어깨 포인트 : 왼쪽 5 오른쪽 6 환산하면 15 , 18

                // 왼쪽 발목 = x == 45
                // 왼쪽 어깨 = X == 15일때
                // 왼쪽 엉덩이 = X == 33 일때
                // 왼쪽 무릎 = X == 39 일때

                while(x <= 49){

                    if(outputFeature0.get(x+2) > 0.45){

                        if(stand && sit) {

                            stand = false
                            sit = false

                        }

//                        Log.d("캔버스 함수 여기서 사용됨", "onSurfaceTextureUpdated: x : $x , h : $h , w : $w")

                        // 각 원들의 중심 좌표
                        canvas.drawCircle(outputFeature0.get(x+1)*w, outputFeature0.get(x)*h, 10f, paint)

                        angle = calculateAngle(outputFeature0.get(34)*w,outputFeature0.get(33)*h,
                            outputFeature0.get(40)*w,outputFeature0.get(39)*h,
                            outputFeature0.get(46)*w,outputFeature0.get(45)*h)

                        // 각도가 일정범위안에 들어왔을때 로그 찍힘.
                        if(angle!! <= 120.00 && angle >= 70.00){

                            sit = true

                        }

                        if(angle!! >= 160 && angle <= 180 && sit) {

                            stand = true
                            count ++

                        }

                    }

                    x+=3

                }

                // 수정된 비트맵 표시
                binding.imageView.setImageBitmap(mutable)

            }

        }
    }


    @SuppressLint("MissingPermission")
    fun open_camera(){
        // 카메라 열기
        cameraManager.openCamera(cameraManager.cameraIdList[0], object : CameraDevice.StateCallback() {
            override fun onOpened(p0: CameraDevice) {
                // 캡처 요청 생성
                var captureRequest = p0.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                var surface = Surface(binding.textureView.surfaceTexture)
                captureRequest.addTarget(surface)

                // 캡처 세션 생성
                p0.createCaptureSession(listOf(surface), object: CameraCaptureSession.StateCallback(){
                    override fun onConfigured(p0: CameraCaptureSession) {
                        // 프레임 캡처 시작
                        p0.setRepeatingRequest(captureRequest.build(),null,null)
                    }

                    override fun onConfigureFailed(p0: CameraCaptureSession) {
                        // 캡처 세션 구성 실패 처리
                    }

                }, handler)
            }

            override fun onDisconnected(camera: CameraDevice) {
                // 카메라 연결 해제 처리
            }

            override fun onError(camera: CameraDevice, error: Int) {
                // 카메라 오류 처리
            }

        }, handler)
    }



    //권한
    fun get_permissions(){
        // 카메라 권한 확인 및 요청
        if(requireActivity().checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA),101)
        }
    }





    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 카메라 권한이 부여되지 않은 경우 다시 요청
        if(grantResults[0] != PackageManager.PERMISSION_GRANTED) get_permissions()
    }



    // 각도 구하는 메서드
    fun calculateAngle(x1: Float,y1: Float,x2: Float,y2: Float,x3: Float, y3: Float): Double? {
        val vec1 = Pair(x1 - x2, y1 - y2)
        val vec2 = Pair(x3 - x2, y3 - y2)

        // 두 벡터의 내적을 계산합니다.
        val dotProduct = vec1.first * vec2.first + vec1.second * vec2.second

        // 각 벡터의 크기를 계산합니다.
        val magnitude1 = sqrt(vec1.first.pow(2) + vec1.second.pow(2))
        val magnitude2 = sqrt(vec2.first.pow(2) + vec2.second.pow(2))

        if (magnitude1 == 0.0f || magnitude2 == 0.0f) {
            return null
        }

        val cosTheta = dotProduct / (magnitude1 * magnitude2)

        // 아크코사인을 사용하여 각도를 계산합니다.
        val angleRad = acos(cosTheta)

        // 라디안에서 도로 변환합니다.
        val angleDeg = Math.toDegrees(angleRad.toDouble())

        return angleDeg
    }


}