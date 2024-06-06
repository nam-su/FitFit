package com.example.fitfit.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.data.User
import com.example.fitfit.databinding.CustomDialogTwoButtonBinding
import com.example.fitfit.databinding.FragmentUserEditBinding
import com.example.fitfit.viewModel.UserEditViewModel

class UserEditFragment : Fragment() {

    private val TAG = "유저 에딧 프래그먼트"

    lateinit var binding: FragmentUserEditBinding
    lateinit var customDialogBinding: CustomDialogTwoButtonBinding
    lateinit var dialog: AlertDialog
    private lateinit var userEditViewModel: UserEditViewModel

    private val activityResultImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                userEditViewModel.setImageUri(uri,requireActivity())
            }
        }
    }


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_edit,container,false)
        customDialogBinding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog_two_button, null, false)

        setVariable()
        permissionCheck()
        setCircleImageView()

        return binding.root

    } // onCreateView()



    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        setObserve()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        userEditViewModel = UserEditViewModel()
        binding.userEditViewModel = userEditViewModel

        userEditViewModel.setUserInformation()

    } // setVariable()



    //setListener(){
    private fun setListener(){

        binding.imageViewBack.setOnClickListener{
            this.findNavController().popBackStack()
            (activity as MainActivity).visibleBottomNavi()
        }

        binding.imageViewAdd.setOnClickListener {
            activityResultImage.launch(userEditViewModel.getGalleryIntent())
        }
        
        //nickname 다르게 입력할때마다 유효성 체크
        binding.editTextNickname.addTextChangedListener {
            userEditViewModel.validationNickname(it.toString())
        }

        //완료버튼 클릭 리스너
        binding.textViewComplete.setOnClickListener {
            setCustomDialog(getString(R.string.confirm),getString(R.string.profileEditDialogContent))
        }

        customDialogBinding.textViewButtonOk.setOnClickListener {
            //통신 처리
            userEditViewModel.profileEdit(requireActivity(),binding.editTextNickname.text.toString())
            dialog.dismiss()
        }

        customDialogBinding.textViewCancel.setOnClickListener {
            dialog.dismiss()
        }

    }



    //setObserve()
    private fun setObserve(){

        //이미지 변경 관찰
        userEditViewModel.selectedImageUri.observe(viewLifecycleOwner) {

            //이미지뷰에 글라이드 사용
                Glide.with(this).load(it).into(binding.circleImageViewUserProfile)

            //완료버튼 뷰 세팅
            setTextViewComplete(userEditViewModel.isNicknameValid.value)

        }

        //닉네임 유효성 검사
        userEditViewModel.isNicknameValid.observe(viewLifecycleOwner){

            if(!userEditViewModel.selectedImageUri.isInitialized) {
                if (it && userEditViewModel.nickname.value != binding.editTextNickname.text.toString()) {
                    setTextViewComplete(true)
                } else {
                    setTextViewComplete(false)
                }
            }else{
                setTextViewComplete(it)
            }

        }

        //통신 결과 관찰
        userEditViewModel.profileEditResult.observe(viewLifecycleOwner){
            Log.d(TAG, "setObserve: $it")
            when(it){
                "success" -> {
                    Toast.makeText(requireContext(), "프로필 수정이 완료 되었습니다.", Toast.LENGTH_SHORT).show()
                }
                "nicknameDuplicate" -> Toast.makeText(requireContext(), "사용할 수 없는 닉네임 입니다.", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(requireContext(), "통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
            this.findNavController().popBackStack()
            (activity as MainActivity).visibleBottomNavi()
        }

        }


    //완료할수있는지 여부에 따른 뷰
    private fun setTextViewComplete(it: Boolean?){

        when(it){

            true -> {
                binding.textViewComplete.isEnabled = true
                binding.textViewComplete.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            }

            else -> {
                binding.textViewComplete.isEnabled = false
                binding.textViewComplete.setTextColor(ContextCompat.getColor(requireContext(), R.color.halfGrey))
            }

        }
    } //setTextViewComplete()



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

    }



    //권한 요청
    private fun permissionCheck(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
    }



    //프로필이미지뷰 셋
    private fun setCircleImageView(){

        Log.d(TAG, "setCircleImageView: ${userEditViewModel.baseUrl+userEditViewModel.profileImagePath.value}")
        Glide.with(this)
                //baseurl+쉐어드의 이미지경로
            .load(userEditViewModel.baseUrl+userEditViewModel.profileImagePath.value)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.loading)
            .into(binding.circleImageViewUserProfile)

    }

    }


