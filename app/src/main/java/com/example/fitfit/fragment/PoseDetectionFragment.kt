package com.example.fitfit.fragment

import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.FragmentPoseDetectionBinding
import com.example.fitfit.viewModel.PoseDetectionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class PoseDetectionFragment : Fragment() {

    private val TAG = "포즈추정 프래그먼트"
    // 데이터 바인딩 객체
    private lateinit var binding: FragmentPoseDetectionBinding

    // ViewModel 객체
    private lateinit var poseDetectionViewModel: PoseDetectionViewModel

    // bundle 로 받는 이용자가 고른 운동 이름
    private lateinit var exerciseName: String

    // tts 객체
    lateinit var tts: TextToSpeech


    // onCreateView 메서드는 Fragment의 뷰를 생성합니다.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 데이터 바인딩 설정
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pose_detection, container, false)

        return binding.root
    }

    // onViewCreated 메서드는 뷰가 생성된 후 호출됩니다.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 필요한 권한을 체크합니다.
        checkPermissions()

        setVariable()
        setObserve()
        setListener()

    } // onViewCreated


    // 변수 초기화
    private fun setVariable() {

        binding.lifecycleOwner = this
        poseDetectionViewModel = PoseDetectionViewModel()
        binding.poseDetectionViewModel = poseDetectionViewModel

        // ViewModel을 초기화합니다.
        poseDetectionViewModel.initialize(requireContext().applicationContext)

        exerciseName = requireArguments().getString("exerciseName").toString()



    } // setVariable


    //리스너 관련 메서드
    private fun setListener(){

        // TextureView의 SurfaceTextureListener를 설정합니다.
        binding.textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            // TextureView가 사용 가능해졌을 때 호출됩니다.
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {

                Log.d(TAG, "onSurfaceTextureAvailable: 1번")
                poseDetectionViewModel.openCamera(binding.textureView)

                // tts 초기화 (비동기로 이루어짐.) 초기화 되자 마자 시작 문구 출력
                tts = TextToSpeech(requireContext(),TextToSpeech.OnInitListener {

                    Log.d(TAG, "onSurfaceTextureAvailable: 4번")

                    tts.language = Locale.KOREA
                    tts.speak("$exerciseName 시작합니다.",TextToSpeech.QUEUE_FLUSH,null,null)
                    tts.speak("올바른 자세로 서주세요",TextToSpeech.QUEUE_ADD,null,null)

                })

            }

            // TextureView의 크기가 변경되었을 때 호출됩니다.
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

                Log.d(TAG, "onSurfaceTextureAvailable: 2번")

            }

            // TextureView가 파괴될 때 호출됩니다.
            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            // TextureView가 업데이트될 때 호출됩니다.
            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

                Log.d(TAG, "onSurfaceTextureAvailable: 3번")
                poseDetectionViewModel.checkExerciseCount(binding.textureView.bitmap!!,exerciseName)

            }
        }

        binding.buttonTest.setOnClickListener {

            startMediaPlayer(R.raw.exercise_signal)

        }

    } // setListener()


    //Observe 관련 메서드
    private fun setObserve(){

        // ViewModel의 bitmap LiveData를 관찰하여 UI를 업데이트합니다.
        poseDetectionViewModel.bitmap.observe(viewLifecycleOwner, Observer { bitmap ->
            binding.imageView.setImageBitmap(bitmap)
        })


        // 운동 카운트 감지.
        poseDetectionViewModel.checkExerciseCount.observe(viewLifecycleOwner) {

            if(it) {

                // 감지 리스너를 새로 초기화 해줘야 데이터가 중첩 안됨.
                // 초기화 해주지 않으면 감지가 계속 호출될때 마다 변수 감지해서 중첩됨.
                tts.speak("운동이 끝났습니다",TextToSpeech.QUEUE_FLUSH,null,null)

                binding.textureView.surfaceTextureListener = null

                finishExercise(exerciseName)

            }

        }

    } //setObserve()


    // 운동 효과음 재생하는 메서드
    private fun startMediaPlayer(music : Int) {

        val mediaPlayer = MediaPlayer.create(requireContext(), music)
        mediaPlayer.isLooping = false

        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }

        mediaPlayer.start()

    } // startMediaPlayer()


    // 운동 개수를 다 채웠을때 호출하는 메서드
    private fun finishExercise(exerciseName: String) {

        CoroutineScope(Dispatchers.Main).launch {

            delay(500)

            // 쉐어드에 데이터 갱신
            poseDetectionViewModel.updatePoseExercise(exerciseName)

            this@PoseDetectionFragment.findNavController().popBackStack()

        }

    } // finishExercise()


    // 카메라 권한을 체크
    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 101)
        }
    }


    // 권한 요청 결과를 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            poseDetectionViewModel.openCamera(binding.textureView)
        } else {
            checkPermissions()
        }
    }
}