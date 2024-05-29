package com.example.fitfit.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.util.Log
import android.view.Surface
import android.view.TextureView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.model.PoseDetectionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PoseDetectionViewModel : ViewModel() {

    private val TAG = "포즈 추정 뷰 모델"
    // 포즈 감지 모델 객체
    private lateinit var poseDetectionModel: PoseDetectionModel
    // 카메라 매니저 객체
    private lateinit var cameraManager: CameraManager

    // 추론된 비트맵을 저장하는 LiveData
    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap> get() = _bitmap   // 외부에서 읽을 수 있는 비트맵 LiveData

    // 카운트를 저장하는 LiveData
    private val _count = MutableLiveData<Int>()
    val count: LiveData<Int> get() = _count        // 외부에서 읽을 수 있는 카운트 LiveData



    // ViewModel 초기화 메서드
    fun initialize(context: Context) {
        // 포즈 감지 모델 초기화
        poseDetectionModel = PoseDetectionModel(context)
        // 카메라 매니저 초기화
        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }



    // 카메라 열기 메서드
    @SuppressLint("MissingPermission")
    fun openCamera(textureView: TextureView) {
        viewModelScope.launch(Dispatchers.Main) {
        cameraManager.openCamera(cameraManager.cameraIdList[0], object : CameraDevice.StateCallback() {

            override fun onOpened(cameraDevice: CameraDevice) {
                viewModelScope.launch(Dispatchers.Main) {

                    val captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    val surface = Surface(textureView.surfaceTexture)
                    captureRequest.addTarget(surface)

                    cameraDevice.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
                            override fun onConfigured(session: CameraCaptureSession) {
                                viewModelScope.launch(Dispatchers.Main) {
                                    session.setRepeatingRequest(captureRequest.build(), null, null)  // 반복 요청 설정
                                } // session.setRepeatingRequest 쪽 코루틴 끝
                            }

                            override fun onConfigureFailed(session: CameraCaptureSession) {
                                // 설정 실패 처리
                            }
                        },
                        null
                    )
                } // 열려있을 때 코루틴 마지막 단
            }

            override fun onDisconnected(camera: CameraDevice) {
                // 카메라 연결 끊김 처리
                Log.d(TAG, "onDisconnected: ")
            }

            override fun onError(camera: CameraDevice, error: Int) {
                // 카메라 에러 처리
                Log.d(TAG, "onError: $error")
            }
        }, null)
    } // cameraManager.openCamera 코루틴 끝
    } //openCamera()



    // 이미지 처리 메서드
    fun processImage(bitmap: Bitmap) {
        viewModelScope.launch{
        // 이미지 처리
        val (processedBitmap, detectedCount) = poseDetectionModel.processImage(bitmap)
        // 처리된 비트맵 업데이트
        _bitmap.postValue(processedBitmap)
        // 감지된 카운트 업데이트
        _count.postValue(detectedCount)
        }
    }



    // ViewModel이 소멸될 때 호출되는 메서드
    override fun onCleared() {
        super.onCleared()
        // 모델 리소스 해제
        poseDetectionModel.close()
    }
}
