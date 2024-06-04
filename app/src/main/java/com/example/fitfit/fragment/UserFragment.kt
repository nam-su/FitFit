package com.example.fitfit.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.CustomDialogTwoButtonBinding
import com.example.fitfit.databinding.FragmentUserBinding
import com.example.fitfit.viewModel.UserViewModel

class UserFragment : Fragment() {

    private val TAG = "유저 프래그먼트"

    lateinit var binding: FragmentUserBinding
    lateinit var customDialogBinding: CustomDialogTwoButtonBinding
    lateinit var userViewModel: UserViewModel

    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user,container,false)

        setVariable()
        setBackPressed()

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

        userViewModel = UserViewModel()
        binding.userViewModel = userViewModel

        userViewModel.setUserInformation()



    } // setVariable()



    //setListener(){
    private fun setListener(){

        //설정 버튼 누르기
        binding.imageButtonSetting.setOnClickListener {

            setAllMenuFalse()

            binding.drawerLayout.openDrawer(GravityCompat.END)
        }



        // NavigationView의 아이템 선택 리스너 설정
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            userViewModel.selectItem(menuItem)
            binding.drawerLayout.closeDrawer(GravityCompat.END)
            true
        }
    }



    //setObserve()
    private fun setObserve(){

        //메뉴 선택에 대한 리스너
        userViewModel.selectedMenuItem.observe(viewLifecycleOwner){ setSelectedMenuItem(it) }

        //로그아웃 버튼 클릭 관찰
        userViewModel.isLogoutButtonClick.observe(viewLifecycleOwner){ setIsLogoutButtonClick(it) }

        //다이얼로그 회원 탈퇴 절차 진행 버튼 클릭 관찰
        userViewModel.isProgressButtonClick.observe(viewLifecycleOwner){ setIsProgressButtonClick(it) }

        //다이얼로그 회원 탈퇴 버튼 클릭 관찰
        userViewModel.isWithdrawalButtonClick.observe(viewLifecycleOwner){ setIsWithdrawalButtonClick(it) }

        //다이얼로그 회원 탈퇴 버튼 클릭 관찰
        userViewModel.isWithdrawalSuccess.observe(viewLifecycleOwner) { setIsWithdrawalSuccess(it) }

    } //setObserve()



    // 모든 메뉴 아이템의 체크 상태를 false로 설정하는 메서드
    private fun setAllMenuFalse(){

        for (i in 0 until binding.navigationView.menu.size()) {
            val item = binding.navigationView.menu.getItem(i)
            item.isChecked = false
        }

    } // setAllMenuFalse()



    // 로그아웃 버튼 클릭 상태에 대한 처리
    private fun setIsLogoutButtonClick(it:Boolean){

        if(it){
            // 로그아웃 버튼 클릭 감지하면 로그인 프래그먼트로 이동 후 로그아웃 false값으로 변경
            this.findNavController().navigate(R.id.action_userFragment_to_loginFragment, null,
                NavOptions.Builder().setPopUpTo(findNavController().graph.startDestinationId, true).build())

            userViewModel.setIsLogoutButtonClick(false)
            //바텀 네비게이션 GONE 처리
            (activity as MainActivity).goneBottomNavi()
        }

    } //setIsLogoutButtonClick()



    // 다이얼로그 내의 진행버튼 클릭 상태에 대한 처리
    private fun setIsProgressButtonClick(it: Boolean){

        if(it){
            setCustomDialog(getString(R.string.withdrawal),getString(R.string.withdrawalDialogContent))
            customDialogBinding.textViewButtonOk.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            userViewModel.setIsProgressButtonClick(false)
        }

    }



    // 다이얼로그 내의 회원탈퇴 클릭 상태에 대한 처리
    private fun setIsWithdrawalButtonClick(it: Boolean){

        if(it){
            userViewModel.withdrawal()
            userViewModel.setIsWithdrawalButtonClick(false)
        }

    }



    // 회원탈퇴 성공 여부에 대한 처리
    private fun setIsWithdrawalSuccess(it: String){
        when(it){
            "success" -> {
                // 로그인 프래그먼트로 이동 후 로그아웃 false값으로 변경
                this.findNavController().navigate(R.id.action_userFragment_to_loginFragment, null,
                    NavOptions.Builder().setPopUpTo(findNavController().graph.startDestinationId, true).build())

                userViewModel.setIsWithdrawalButtonClick(false)

                //바텀 네비게이션 GONE 처리
                (activity as MainActivity).goneBottomNavi()

                Toast.makeText(requireActivity(), "회원탈퇴가 정상적으로 처리 되었습니다.",Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireContext(), "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }//setIsWithdrawalSuccess()

    

    //drawerLayout에 체크된 메뉴아이템 관찰 대한 처리
    private fun setSelectedMenuItem(it : MenuItem){

        // 네비게이션 메뉴 선택 효과 주기
        // isChecked = true : 메뉴 텍스트 굵기가 굵어짐.
        it.isChecked = true

        Log.d(TAG, "setObserve: $it")
        when(it.toString()){
            "프로필 수정"  -> {
                //프로필 수정 프래그먼트로 이동
                Log.d(TAG, "setObserve: 프로필 수정 프래그먼트로 이동")
            }
           "비밀번호 변경"   -> {
                //비밀번호 변경 프래그먼트로 이동
                Log.d(TAG, "setObserve: 비밀번호 변경 프래그먼트로 이동")
            }
            "로그아웃"   -> {
                //로그아웃 다이얼로그
                Log.d(TAG, "setObserve: 로그아웃 다이얼로그")
                setCustomDialog(getString(R.string.logout),getString(R.string.logoutDialogContent))
                customDialogBinding.textViewButtonOk.setTextColor(ContextCompat.getColor(requireContext(), R.color.personal))
            }
            "회원탈퇴"  -> {
                //회원탈퇴 진행 다이얼로그
                Log.d(TAG, "setObserve: 회원탈퇴 다이얼로그")
                setCustomDialog(getString(R.string.progress),getString(R.string.progressDialogContent))
                customDialogBinding.textViewButtonOk.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))

            }
            else -> {
                Log.d(TAG, "setObserve: 빈칸고름")
            }
        }
    }



    //커스텀 다이얼로그 띄우기
    private fun setCustomDialog(buttonOkText: String, content:String){

        //데이터바인딩 준비
        val inflater = LayoutInflater.from(requireContext())
        customDialogBinding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog_two_button, null, false)

        //다이얼로그 생성
        val dialog = AlertDialog.Builder(requireContext())
            .setView(customDialogBinding.root)
            .setCancelable(true)
            .create()

        //뒷배경 투명으로 바꿔서 둥근모서리 보이게
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customDialogBinding.textViewContent.text = content
        customDialogBinding.textViewButtonOk.text = buttonOkText

        dialog.show()

        customDialogBinding.textViewButtonOk.setOnClickListener {
            userViewModel.setOnDialogOkButtonClick(buttonOkText)
            dialog.dismiss()
        }

        customDialogBinding.textViewCancel.setOnClickListener {
            setAllMenuFalse()
            dialog.dismiss()

        }
    }



    // DrawerLayout을 닫는 메서드 추가
    private fun closeDrawerIfNeeded() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }
    }




    //뒤로가기 버튼 제어
    private fun setBackPressed() {

        requireActivity().onBackPressedDispatcher.addCallback(this){

            closeDrawerIfNeeded() // 뒤로가기 버튼을 누를 때 DrawerLayout을 닫습니다.

            if (!binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                requireActivity().finish()
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        closeDrawerIfNeeded()
    }

}