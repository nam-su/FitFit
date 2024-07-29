package com.example.fitfit.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.CustomDialogNetworkDisconnectBinding
import com.example.fitfit.databinding.FragmentPoseDetectionBinding
import com.example.fitfit.function.MyApplication
import com.example.fitfit.viewModel.PoseDetectionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.reflect.KMutableProperty0

class PoseDetectionFragment : Fragment() {

    private val TAG = "포즈추정 프래그먼트"

    // 데이터 바인딩 객체
    private lateinit var binding: FragmentPoseDetectionBinding

    // ViewModel 객체
    private lateinit var poseDetectionViewModel: PoseDetectionViewModel

    // bundle 로 받는 이용자가 고른 운동 이름
    private lateinit var exerciseName: String

    // 뒤로가기 버튼 콜백 객체
    private lateinit var callback: OnBackPressedCallback

    // 네트워크 연결 x 다이얼로그 바인딩
    lateinit var customNetworkDialogBinding: CustomDialogNetworkDisconnectBinding

    // tts 객체
    private var tts: TextToSpeech? = null

    private var lastSpokenMessage: String? = null

    private var isSpeakingCoolDown = false // 잘못된 자세시 TTS 호출 쿨다운 상태를 추적하는 변수

    private var isAccuracySpeakingCoolDown = false // TTS 호출 쿨다운 상태를 추적하는 변수

    private var isStartExercise = false

    // 권한 요청 런처 객체
    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    // onAttach
    override fun onAttach(context: Context) {
        super.onAttach(context)

        setOnBackPressed()

    } // onAttach


    // onCreateView 메서드는 Fragment의 뷰를 생성합니다.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // 데이터 바인딩 설정
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pose_detection, container, false)
        customNetworkDialogBinding = DataBindingUtil.inflate(inflater,R.layout.custom_dialog_network_disconnect,null,false)

        return binding.root

    } // onCreateView()


    // onViewCreated 메서드는 뷰가 생성된 후 호출됩니다.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        initRequestPermissionLauncher()

        checkPermissions()
        setObserve()
        setListener()

    } // onViewCreated


    // 변수 초기화
    private fun setVariable() {

        binding.lifecycleOwner = this

        exerciseName = requireArguments().getString("exerciseName").toString()

        poseDetectionViewModel = PoseDetectionViewModel()
        binding.poseDetectionViewModel = poseDetectionViewModel

        // ViewModel을 초기화합니다.
        poseDetectionViewModel.initialize(requireContext().applicationContext,exerciseName)

    } // setVariable


    // 리스너 관련 메서드
    private fun setListener(){

        // 운동 중단하기 버튼 눌렀을 때
        binding.buttonStopPoseExercise.setOnClickListener {

            tts?.stop()
            tts?.shutdown()
            findNavController().popBackStack()

        }

    } // setListener()


    // 권한요청 런처 초기화
    private fun initRequestPermissionLauncher() {

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->

            if (isGranted) {

                setSurfaceTextureListener()
                poseDetectionViewModel.openCamera(binding.textureView)
                initTextToSpeech()

            } else {

                findNavController().popBackStack()

            }

        }

    } // initRequestPermissionLauncher()


    // tts 초기화 및 초기 tts
    private fun initTextToSpeech() {

        tts = TextToSpeech(requireContext()) { status ->

            if (status == TextToSpeech.SUCCESS) {

                tts?.language = Locale.KOREA

                // 여기서 시작 자세 안내 해주기.
                poseDetectionViewModel.startPoseExercise(exerciseName).speak()

                CoroutineScope(Dispatchers.Main).launch {

                    delay(10000)
                    isStartExercise = true

                }

            }

        }

    } // initTextToSpeech()


    //Observe 관련 메서드
    private fun setObserve(){

        // ViewModel의 bitmap LiveData를 관찰하여 UI를 업데이트합니다.
        poseDetectionViewModel.bitmap.observe(viewLifecycleOwner, Observer { bitmap ->

            binding.imageView.setImageBitmap(bitmap)

        })

        // 운동 시작 후 동작인식 정확도 측정감지
        poseDetectionViewModel.checkAccuracy.observe(viewLifecycleOwner) { accuracy ->

            if (isStartExercise) {

                if (!accuracy && !isAccuracySpeakingCoolDown) {
                    "동작을 감지할 수 없습니다".speak()
                    triggerCoolDown(::isAccuracySpeakingCoolDown)
                }

            }

        }

        // 운동 카운트 감지.
        poseDetectionViewModel.checkExerciseCount.observe(viewLifecycleOwner) {

            if(it) {

                // 감지 리스너를 새로 초기화 해줘야 데이터가 중첩 안됨.
                // 초기화 해주지 않으면 감지가 계속 호출될때 마다 변수 감지해서 중첩됨.
                "운동이 끝났습니다".speak()

                binding.textureView.surfaceTextureListener = null

                finishExercise(exerciseName)

            }

        }


        // 잘못된 동작 감지
        poseDetectionViewModel.checkBadPose.observe(viewLifecycleOwner) { message ->

            if (message.isNotEmpty() && !isSpeakingCoolDown) {

                message.speak()

                lastSpokenMessage = message

                triggerCoolDown(::isSpeakingCoolDown)

            }

        }

    } // setObserve()


    // textureView 관련 초기화
    private fun setSurfaceTextureListener() {

        binding.textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {

                Log.d(TAG, "onSurfaceTextureAvailable")
                poseDetectionViewModel.openCamera(binding.textureView)
                initTextToSpeech()

            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean = false
            
            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

                poseDetectionViewModel.checkExerciseCount(binding.textureView.bitmap!!, exerciseName)

            }

        }

    } // setSurfaceTextureListener()


    // 쿨다운 시작 메서드
    private fun triggerCoolDown(coolDownFlag: KMutableProperty0<Boolean>) {

        coolDownFlag.set(true)
        CoroutineScope(Dispatchers.Main).launch {

            delay(3000)

            coolDownFlag.set(false)

        }

    } // triggerCoolDown


    // tts speak 메서드
    private fun String.speak() {

        tts?.speak(this, TextToSpeech.QUEUE_FLUSH, null, null)

    } // speak


    // 운동 개수를 다 채웠을때 호출하는 메서드
    private fun finishExercise(exerciseName: String) {

        CoroutineScope(Dispatchers.Main).launch {

            delay(500)

            // 인터넷 연결 x
            if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                setNetworkCustomDialog()

                // 인터넷 연결 o
            } else {

                // 서버에 데이터 갱신
                poseDetectionViewModel.updatePoseExercise(exerciseName)

                this@PoseDetectionFragment.findNavController().popBackStack()

            }

        }

    } // finishExercise()


    // 카메라 권한을 체크
    private fun checkPermissions() {

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)

        } else {

            setSurfaceTextureListener()

        }

    } // checkPermissions()


    //커스텀 다이얼로그 띄우기
    private fun setNetworkCustomDialog(){

        // 부모가 있는지 확인하고, 있다면 부모에서 제거
        customNetworkDialogBinding.root.parent?.let {
            (it as ViewGroup).removeView(customNetworkDialogBinding.root)
        }

        //다이얼로그 생성
        val dialog = AlertDialog.Builder(requireContext())
            .setView(customNetworkDialogBinding.root)
            .setCancelable(true)
            .create()

        //뒷배경 투명으로 바꿔서 둥근모서리 보이게
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customNetworkDialogBinding.textViewButtonOk.setOnClickListener {

            dialog.dismiss()
            findNavController().popBackStack()

        }

        dialog.setOnCancelListener {

            dialog.dismiss()
            findNavController().popBackStack()

        }

        dialog.show()

    } // setNetworkCustomDialog()


    // 뒤로가기 버튼 눌렀을 때
    private fun setOnBackPressed() {

        callback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                tts?.stop()
                tts?.shutdown()
                findNavController().popBackStack()

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(this,callback)

    } // onBackPressed()

}