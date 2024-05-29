package com.example.fitfit.fragment

import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.FragmentPoseDetectionBinding
import com.example.fitfit.viewModel.PoseDetectionViewModel

class PoseDetectionFragment : Fragment() {

    private val TAG = "포즈추정 프래그먼트"
    // 데이터 바인딩 객체
    private lateinit var binding: FragmentPoseDetectionBinding

    // ViewModel 객체
    private lateinit var poseDetectionViewModel: PoseDetectionViewModel

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

    }

    // 변수 초기화
    private fun setVariable() {

        binding.lifecycleOwner = this
        poseDetectionViewModel = PoseDetectionViewModel()
        binding.poseDetectionViewModel = poseDetectionViewModel

        // ViewModel을 초기화합니다.
        poseDetectionViewModel.initialize(requireContext().applicationContext)

    } // setVariable



    //리스너 관련 메서드
    private fun setListener(){

        // TextureView의 SurfaceTextureListener를 설정합니다.
        binding.textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            // TextureView가 사용 가능해졌을 때 호출됩니다.
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                poseDetectionViewModel.openCamera(binding.textureView)
            }

            // TextureView의 크기가 변경되었을 때 호출됩니다.
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}

            // TextureView가 파괴될 때 호출됩니다.
            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            // TextureView가 업데이트될 때 호출됩니다.
            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                poseDetectionViewModel.processImage(binding.textureView.bitmap!!)
            }
        }

    } // setListener()



    //Observe 관련 메서드
    private fun setObserve(){

        // ViewModel의 bitmap LiveData를 관찰하여 UI를 업데이트합니다.
        poseDetectionViewModel.bitmap.observe(viewLifecycleOwner, Observer { bitmap ->
            binding.imageView.setImageBitmap(bitmap)
        })

        // ViewModel의 count LiveData를 관찰하여 UI를 업데이트합니다.
        poseDetectionViewModel.count.observe(viewLifecycleOwner, Observer { count ->
            binding.textViewCount.text = count.toString()
        })

    } //setObserve()



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