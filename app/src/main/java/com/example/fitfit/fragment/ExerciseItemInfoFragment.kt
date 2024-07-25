package com.example.fitfit.fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.adapter.ExerciseItemInfoAdapter
import com.example.fitfit.databinding.CustomDialogTwoButtonBinding
import com.example.fitfit.databinding.FragmentExerciseItemInfoBinding
import com.example.fitfit.viewModel.ExerciseItemInfoViewModel

class ExerciseItemInfoFragment : Fragment() {

    val TAG = "운동아이템정보 프래그먼트"

    lateinit var binding: FragmentExerciseItemInfoBinding
    lateinit var exerciseItemInfoViewModel: ExerciseItemInfoViewModel

    lateinit var dialog: AlertDialog
    lateinit var customDialogBinding: CustomDialogTwoButtonBinding

    lateinit var exerciseName: String

    private lateinit var callback: OnBackPressedCallback


    // onAttach
    override fun onAttach(context: Context) {
        super.onAttach(context)

        setOnBackPressed()

    } // onAttach()


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_exercise_item_info,container,false)
        customDialogBinding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog_two_button, null, false)

        return binding.root

    } // onCreateView()


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setObserve()
        setPageChangeListener()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        exerciseName = requireArguments().getString("exerciseName").toString()

        exerciseItemInfoViewModel = ExerciseItemInfoViewModel(exerciseName)
        binding.exerciseItemInfoViewModel = exerciseItemInfoViewModel
        binding.lifecycleOwner = this

        binding.viewPager.adapter = ExerciseItemInfoAdapter(exerciseItemInfoViewModel.getExerciseItemInfo())

        binding.dotsIndicator.setViewPager2(binding.viewPager)

    } // setVariable()


    // 변수 감지 메서드
    private fun setObserve() {

        // 현재 인덱스 observe
        exerciseItemInfoViewModel.exerciseItemIndex.observe(viewLifecycleOwner) {

            when(it < 0) {

                true -> {

                    findNavController().popBackStack()
                    (activity as MainActivity).visibleBottomNavi()

                }

                else -> {

                    // it에 맞게 어댑터 포지션을 지정
                    (binding.viewPager.adapter as? ExerciseItemInfoAdapter)?.let { _ ->

                        if (binding.viewPager.currentItem != it) {

                            binding.viewPager.currentItem = it

                        }

                    }

                }

            }

        }

        exerciseItemInfoViewModel.isExerciseItemInfoPrimium.observe(viewLifecycleOwner) {
            
            if(it) {

                binding.imageViewPrimiumBadge.visibility = View.VISIBLE

            }

        }

        exerciseItemInfoViewModel.isUserSubscribe.observe(viewLifecycleOwner) {

            // 유저 구독이 되어 있는 경우
            if(it) {

                findNavController().navigate(R.id.exerciseFragment)
                (activity as MainActivity).visibleBottomNavi()

            // 유저 구독이 안되어 있는 경우
            } else {

                // 다이얼로그 띄워준다.
                setCustomDialog()

            }

        }

    } // setObserve()


    //  viewPager page 바뀔때 호출하는 메서드
    private fun setPageChangeListener() {

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {

                super.onPageSelected(position)
                exerciseItemInfoViewModel.setExerciseItemIndex(position)

            }

        })

    } // setPageChangedListener()


    //커스텀 다이얼로그 띄우기
    private fun setCustomDialog(){

        // 부모가 있는지 확인하고, 있다면 부모에서 제거
        customDialogBinding.root.parent?.let {
            (it as ViewGroup).removeView(customDialogBinding.root)
        }

        //다이얼로그 생성
        dialog = AlertDialog.Builder(requireContext())
            .setView(customDialogBinding.root)
            .setCancelable(true)
            .create()

        //뒷배경 투명으로 바꿔서 둥근모서리 보이게
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customDialogBinding.textViewContent.text = "구독 후 이용 가능한 서비스 입니다."
        customDialogBinding.textViewButtonOk.text = "확인"
        customDialogBinding.textViewButtonOk.setTextColor(ContextCompat.getColor(requireContext(), R.color.personal))

        // 다이얼로그 확인 버튼 눌렀을 때
        customDialogBinding.textViewButtonOk.setOnClickListener {

            // 결제 프래그먼트로 전환
            findNavController().navigate(R.id.payFragment)
            dialog.dismiss()

        }

        // 다이얼로그 취소 버튼 눌렀을 때
        customDialogBinding.textViewCancel.setOnClickListener {

            // 다이얼로그 취소
            dialog.dismiss()

        }

        dialog.show()

    } // setCustomDialog()


    // 뒤로가기 클릭 리스너
    private fun setOnBackPressed() {

        callback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                // 다이얼로그가 보여지고 있는 경우에는 다이얼로그 dismiss()
                if (dialog.isShowing) {

                    dialog.dismiss()

                } else {

                    binding.imageButtonBackExerciseInfo.callOnClick()

                }

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(this,callback)

    } // setOnBackPressed()


}