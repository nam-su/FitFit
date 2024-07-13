package com.example.fitfit.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.os.Build
import android.util.Log
import android.view.Surface
import android.view.TextureView
import androidx.annotation.RequiresApi

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.model.PoseDetectionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PoseDetectionViewModel() : ViewModel() {

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

    private val _checkExerciseCount = MutableLiveData<Boolean>()
    val checkExerciseCount: LiveData<Boolean> get() = _checkExerciseCount

    private val _checkBadPose = MutableLiveData<String>()
    val checkBadPose: LiveData<String> get() = _checkBadPose


    private val _checkAccuracy = MutableLiveData<Boolean>()
    val checkAccuracy: LiveData<Boolean> get() = _checkAccuracy

    // ViewModel 초기화 메서드
    fun initialize(context: Context,exerciseName: String) {

        // 포즈 감지 모델 초기화
        poseDetectionModel = PoseDetectionModel(context,exerciseName)
        // 카메라 매니저 초기화
        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    } // initialize()


    @SuppressLint("MissingPermission") // 권한 재확인 안하기 위한 어노테이션
    fun openCamera(textureView: TextureView): Boolean {

        viewModelScope.launch(Dispatchers.Main) {

            cameraManager.openCamera(cameraManager.cameraIdList[0], object : CameraDevice.StateCallback() {

                @RequiresApi(Build.VERSION_CODES.P)
                override fun onOpened(cameraDevice: CameraDevice) {

                    val surface = Surface(textureView.surfaceTexture)

                    val captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
                        addTarget(surface)
                    }

                    val sessionConfiguration = SessionConfiguration(
                        SessionConfiguration.SESSION_REGULAR, // 또는 SESSION_HIGH_SPEED
                        listOf(OutputConfiguration(surface)),
                        Executors.newSingleThreadExecutor(), // Executor
                        object : CameraCaptureSession.StateCallback() {
                            override fun onConfigured(session: CameraCaptureSession) {
                                session.setRepeatingRequest(captureRequest.build(), null, null)
                            }

                            override fun onConfigureFailed(session: CameraCaptureSession) {
                                // Session configuration failed
                            }
                        }
                    )

                    cameraDevice.createCaptureSession(sessionConfiguration)

                }

                override fun onDisconnected(camera: CameraDevice) {
                    // "Camera disconnected"
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    Log.e(TAG, "Camera error: $error")
                }
            }, null)
        }
        return true

    } //openCamera()


    // 이미지 처리 메서드
    private fun processImage(bitmap: Bitmap, exerciseName: String) {

        // 이미지 처리
        val (processedBitmap, detectedCount) = poseDetectionModel.processImage(bitmap)

        // 처리된 비트맵 업데이트
        _bitmap.postValue(processedBitmap)

        // 감지된 카운트 업데이트
        _count.postValue(detectedCount)

        _checkBadPose.value = poseDetectionModel.checkBadPose

        _checkAccuracy.value = poseDetectionModel.checkAccuracy

    } // processImage()


    // 모델의 카운트가 몇인지 확인 하는 메서드
    fun checkExerciseCount(bitmap: Bitmap, exerciseName: String) {

        if(poseDetectionModel.count == 2) {

            _checkExerciseCount.value = true

        } else {

            _checkExerciseCount.value = false
            processImage(bitmap,exerciseName)

        }

    } // checkExerciseCount()


    // 운동 끝난 후 운동 후 데이터 쉐어드에 갱신 및 통신 응답
    fun updatePoseExercise(exerciseName: String) {

        viewModelScope.launch {

            val response = poseDetectionModel.updatePoseExercise(exerciseName)

            if (response.isSuccessful && response.body() != null) {

                Log.d(TAG, "Exercise data successfully updated on server")

            } else {

                Log.e(TAG, "Failed to update exercise data: ${response.message()}")

            }

        }

    } // updatePoseExercise()


    // ViewModel이 소멸될 때 호출되는 메서드
    override fun onCleared() {
        super.onCleared()

        // 모델 리소스 해제
        poseDetectionModel.close()
        Log.d(TAG, "onCleared:모델 리소스 해제")

    }

}
