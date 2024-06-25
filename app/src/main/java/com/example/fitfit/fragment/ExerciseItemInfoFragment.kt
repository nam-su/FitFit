package com.example.fitfit.fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.CustomDialogTwoButtonBinding
import com.example.fitfit.databinding.FragmentExerciseItemInfoBinding
import com.example.fitfit.viewModel.ExerciseItemInfoViewModel

class ExerciseItemInfoFragment : Fragment() {

    lateinit var binding: FragmentExerciseItemInfoBinding
    lateinit var exerciseItemInfoViewModel: ExerciseItemInfoViewModel

    lateinit var dialog: AlertDialog
    lateinit var customDialogBinding: CustomDialogTwoButtonBinding

    lateinit var exerciseName: String

    private lateinit var callback: OnBackPressedCallback


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_exercise_item_info,container,false)
        customDialogBinding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog_two_button, null, false)

        return binding.root

    } // onCreateView()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setOnBackPressed()

    } // onAttach()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setObserve()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        exerciseName = requireArguments().getString("exerciseName").toString()
        exerciseItemInfoViewModel = ExerciseItemInfoViewModel(exerciseName)
        binding.exerciseItemInfoViewModel = exerciseItemInfoViewModel
        binding.lifecycleOwner = this



    } // setVariable()


    // 변수 감지 메서드
    private fun setObserve() {

        exerciseItemInfoViewModel.exerciseItemIndex.observe(viewLifecycleOwner) {

            when(it < 0) {

                true -> {

                    findNavController().popBackStack()
                    (activity as MainActivity).visibleBottomNavi()

                }
                else -> binding.view.setImageResource(exerciseItemInfoViewModel.setExerciseItemInfoImage())

            }

        }

    } // setObserve()


    //커스텀 다이얼로그 띄우기
    private fun setCustomDialog(buttonOkText: String, content:String){

        //다이얼로그 생성
        dialog = AlertDialog.Builder(requireContext())
            .setView(customDialogBinding.root)
            .setCancelable(true)
            .create()

        //뒷배경 투명으로 바꿔서 둥근모서리 보이게
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customDialogBinding.textViewContent.text = content
        customDialogBinding.textViewButtonOk.text = buttonOkText
        customDialogBinding.textViewButtonOk.setTextColor(ContextCompat.getColor(requireContext(), R.color.personal))

        dialog.show()

    } // setCustomDialog()


    // 뒤로가기 클릭 리스너
    private fun setOnBackPressed() {

        callback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                binding.imageButtonBackExerciseInfo.callOnClick()

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(this,callback)

    } // setOnBackPressed()


}