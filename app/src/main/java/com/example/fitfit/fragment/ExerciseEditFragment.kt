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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.fitfit.R
import com.example.fitfit.adapter.PoseExerciseAdapter
import com.example.fitfit.databinding.CustomDialogNetworkDisconnectBinding
import com.example.fitfit.databinding.CustomDialogTwoButtonBinding
import com.example.fitfit.databinding.FragmentExerciseEditBinding
import com.example.fitfit.function.MyApplication
import com.example.fitfit.viewModel.ExerciseEditViewModel


class ExerciseEditFragment : Fragment() {

    private lateinit var binding: FragmentExerciseEditBinding
    private lateinit var exerciseEditViewModel: ExerciseEditViewModel

    private lateinit var callback: OnBackPressedCallback

    lateinit var customNetworkDialogBinding: CustomDialogNetworkDisconnectBinding
    lateinit var customDialogBinding: CustomDialogTwoButtonBinding

    private lateinit var myPoseExerciseAdapter: PoseExerciseAdapter

    private lateinit var allSquatAdapter: PoseExerciseAdapter
    private lateinit var allPushUpAdapter: PoseExerciseAdapter
    private lateinit var allLungeAdapter: PoseExerciseAdapter
    private lateinit var allLegRaisesAdapter: PoseExerciseAdapter


    // onAttach
    override fun onAttach(context: Context) {
        super.onAttach(context)

        setOnBackPressed()

    } // onAttach


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_exercise_edit,container,false)
        customDialogBinding = DataBindingUtil.inflate(inflater,R.layout.custom_dialog_two_button,null,false)
        customNetworkDialogBinding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog_network_disconnect, null, false)

        return binding.root

    } // onCreateView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setClickListener()
        setObserve()

    } // onViewCreated

    // 변수 초기화 메서드
    private fun setVariable() {

        // 뷰모델 초기화
        exerciseEditViewModel = ExerciseEditViewModel()

        // xml 파일에 뷰모델 연결
        binding.exerciseEditViewModel = exerciseEditViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        // 내 운동 리스트 리사이클러뷰
        myPoseExerciseAdapter =
            PoseExerciseAdapter(exerciseEditViewModel.getSharedMyExerciseList(),true,"true")

        binding.recyclerViewMyPoseExerciseList.adapter = myPoseExerciseAdapter

        // 모든 스쿼트 리스트 리사이클러뷰
        allSquatAdapter =
            PoseExerciseAdapter(exerciseEditViewModel.setAllSquatList(),true,"false")

        binding.recyclerViewAllSquat.adapter = allSquatAdapter

        // 모든 푸시업 리스트 리사이클러뷰
        allPushUpAdapter =
            PoseExerciseAdapter(exerciseEditViewModel.setAllPushUpList(),true,"false")

        binding.recyclerViewAllPushUp.adapter = allPushUpAdapter

        // 모든 런지 리스트 리사이클러뷰
        allLungeAdapter =
            PoseExerciseAdapter(exerciseEditViewModel.setAllLungeList(),true,"false")

        binding.recyclerViewAllLunge.adapter = allLungeAdapter

        // 모든 레그레이즈 리스트 리사이클러뷰
        allLegRaisesAdapter =
            PoseExerciseAdapter(exerciseEditViewModel.setAllLegRaisesList(),true,"false")

        binding.recyclerViewAllLegRaises.adapter = allLegRaisesAdapter

    } // setVariable()


    // observe하는 메서드
    private fun setObserve() {

        // 최대 사이즈 감지 후 토스트
        exerciseEditViewModel.checkMyExerciseListSizeMax.observe(viewLifecycleOwner) {

            if(!it) {

                Toast.makeText(requireContext(),getString(R.string.maximumEditExercise),Toast.LENGTH_SHORT).show()

            }

        }

        // 편집에 대한 통신 성공했을때
        exerciseEditViewModel.isSuccessfulEdit.observe(viewLifecycleOwner){

            when(it){

                true -> {

                    Toast.makeText(requireContext(),getString(R.string.changeMyExerciseList), Toast.LENGTH_SHORT).show()
                    exerciseEditViewModel.setUserCheckList()
                    exerciseEditViewModel.setMyPoseExerciseList()
                    findNavController().popBackStack()

                }

                    else -> Toast.makeText(requireContext(),getString(R.string.networkConnectionIsUnstable), Toast.LENGTH_SHORT).show()

            }

        }

    } // setObserve


    // 클릭 리스너 초기화 하는 메서드
    private fun setClickListener() {

        // 뒤로가기 버튼 눌렀을 때
        binding.imageButtonEditBack.setOnClickListener {

            this.findNavController().navigate(R.id.action_exerciseEditFragment_to_exerciseChoiceFragment)

        }


        // 내 운동리스트에서 삭제 버튼 눌렀을 때
        myPoseExerciseAdapter.exerciseEditItemDeleteButtonClick = object :PoseExerciseAdapter.ExerciseEditItemDeleteButtonClick{

            override fun onDeleteButtonClick(view: View, position: Int) {

                exerciseEditViewModel.deleteExerciseItem(myPoseExerciseAdapter.poseExerciseList,position)
                myPoseExerciseAdapter.notifyDataSetChanged()
                allSquatAdapter.notifyDataSetChanged()
                allPushUpAdapter.notifyDataSetChanged()
                allLungeAdapter.notifyDataSetChanged()
                allLegRaisesAdapter.notifyDataSetChanged()

            }

        }

        // 스쿼트리스트에서 추가 버튼 눌렀을 때
        allSquatAdapter.exerciseEditItemAddButtonClick = object :PoseExerciseAdapter.ExerciseEditItemAddButtonClick{

            override fun onAddButtonClick(view: View, position: Int) {

                if(!exerciseEditViewModel.addExerciseItem(allSquatAdapter.poseExerciseList,position)) {

                    // 다이얼로그 띄워주기.
                    setCustomDialog(getString(R.string.check),getString(R.string.canISubscribe))

                } else {

                    myPoseExerciseAdapter.notifyDataSetChanged()
                    allSquatAdapter.notifyDataSetChanged()

                }

            }

        }

        // 푸시업 리스트에서 추가버튼 눌렀을 때
        allPushUpAdapter.exerciseEditItemAddButtonClick = object :PoseExerciseAdapter.ExerciseEditItemAddButtonClick{

            override fun onAddButtonClick(view: View, position: Int) {

                if(!exerciseEditViewModel.addExerciseItem(allPushUpAdapter.poseExerciseList,position)) {

                    // 다이얼로그 띄워주기.
                    setCustomDialog(getString(R.string.check),getString(R.string.canISubscribe))

                } else {

                    myPoseExerciseAdapter.notifyDataSetChanged()
                    allPushUpAdapter.notifyDataSetChanged()

                }

            }

        }

        // 런지 리스트에서 추가버튼 눌렀을 때
        allLungeAdapter.exerciseEditItemAddButtonClick = object :PoseExerciseAdapter.ExerciseEditItemAddButtonClick{

            override fun onAddButtonClick(view: View, position: Int) {

                if(!exerciseEditViewModel.addExerciseItem(allLungeAdapter.poseExerciseList,position)) {

                    // 다이얼로그 띄워주기.
                    setCustomDialog(getString(R.string.check),getString(R.string.canISubscribe))

                } else {

                    myPoseExerciseAdapter.notifyDataSetChanged()
                    allLungeAdapter.notifyDataSetChanged()

                }

            }

        }

        // 레그레이즈 리스트에서 추가버튼 눌렀을 때
        allLegRaisesAdapter.exerciseEditItemAddButtonClick = object :PoseExerciseAdapter.ExerciseEditItemAddButtonClick{

            override fun onAddButtonClick(view: View, position: Int) {

                if(!exerciseEditViewModel.addExerciseItem(allLegRaisesAdapter.poseExerciseList,position)) {

                    // 다이얼로그 띄워주기.
                    setCustomDialog(getString(R.string.check),getString(R.string.canISubscribe))

                } else {

                    myPoseExerciseAdapter.notifyDataSetChanged()
                    allLegRaisesAdapter.notifyDataSetChanged()

                }

            }

        }

        // 리스트 편집 후 완료 버튼 눌렀을 때
        binding.buttonComplete.setOnClickListener {

            // 인터넷 연결 안되어 있는 경우
            if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                setNetworkCustomDialog()

            } else {

                if(!exerciseEditViewModel.checkMyExerciseListSizeMin()) {

                    Toast.makeText(requireContext(),getString(R.string.minimumEditExercise),Toast.LENGTH_SHORT).show()

                } else {

                    exerciseEditViewModel.setMyPoseExercise()


                }

            }

        }

    } // setClickListener()


    // 뒤로가기 버튼 클릭 했을 때
    private fun setOnBackPressed() {

        callback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                findNavController().popBackStack()

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(this,callback)

    } // setOnBackPressed()


    //커스텀 다이얼로그 띄우기
    private fun setCustomDialog(buttonOkText: String, content:String){

        // 부모가 있는지 확인하고, 있다면 부모에서 제거
        customDialogBinding.root.parent?.let {

            (it as ViewGroup).removeView(customDialogBinding.root)

        }

        //다이얼로그 생성
        val dialog = AlertDialog.Builder(requireContext())
            .setView(customDialogBinding.root)
            .setCancelable(true)
            .create()

        //뒷배경 투명으로 바꿔서 둥근모서리 보이게
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customDialogBinding.textViewButtonOk.setTextColor(ContextCompat.getColor(requireContext(), R.color.personal))

        customDialogBinding.textViewContent.text = content
        customDialogBinding.textViewButtonOk.text = buttonOkText

        dialog.show()

        customDialogBinding.textViewButtonOk.setOnClickListener {

            findNavController().navigate(R.id.payFragment)
            dialog.dismiss()

        }

        customDialogBinding.textViewCancel.setOnClickListener {

            dialog.dismiss()

        }

    } // setCustomDialog()


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

        }

        dialog.setOnCancelListener {

            dialog.dismiss()

        }

        dialog.show()

    } // setCustomDialog()


}