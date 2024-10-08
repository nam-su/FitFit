package com.example.fitfit.fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.CustomDialogNetworkDisconnectBinding
import com.example.fitfit.databinding.CustomDialogTwoButtonBinding
import com.example.fitfit.databinding.FragmentUserBinding
import com.example.fitfit.function.MyApplication
import com.example.fitfit.viewModel.UserViewModel
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback


class UserFragment : Fragment() {

    lateinit var binding: FragmentUserBinding
    lateinit var customDialogBinding: CustomDialogTwoButtonBinding
    lateinit var customNetworkDialogBinding: CustomDialogNetworkDisconnectBinding
    lateinit var customSubscriptionDialogBinding: CustomDialogTwoButtonBinding

    lateinit var dialog: AlertDialog

    lateinit var userViewModel: UserViewModel

    private lateinit var callback: OnBackPressedCallback


    // onAttach
    override fun onAttach(context: Context) {
        super.onAttach(context)

        setOnBackPressed()

    } // onAttach


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user,container,false)
        customNetworkDialogBinding = DataBindingUtil.inflate(inflater,R.layout.custom_dialog_network_disconnect,null,false)
        customSubscriptionDialogBinding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog_two_button, null, false)
        customDialogBinding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog_two_button, null, false)


        setVariable()
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

        (activity as MainActivity).visibleBottomNavi()

        userViewModel = UserViewModel()
        binding.userViewModel = userViewModel

        userViewModel.setUserInformation(getString(R.string.baseUrl))

    } // setVariable()


    // 리스너 초기화
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

    } // setListener()


    // setObserve
    private fun setObserve(){

        //메뉴 선택에 대한 리스너
        userViewModel.selectedMenuItem.observe(viewLifecycleOwner){ setSelectedMenuItem(it) }

        //로그아웃 버튼 클릭 관찰
        userViewModel.isLogoutButtonClick.observe(viewLifecycleOwner){ setIsLogoutButtonClick(it) }

        //다이얼로그 회원 탈퇴 절차 진행 버튼 클릭 관찰
        userViewModel.isProgressButtonClick.observe(viewLifecycleOwner){ setIsProgressButtonClick(it) }

        //다이얼로그 회원 탈퇴 버튼 클릭 관찰
        userViewModel.isWithdrawalButtonClick.observe(viewLifecycleOwner){ setIsWithdrawalButtonClick(it) }

        //다이얼로그 회원 탈퇴 성공 여부 관찰
        userViewModel.isWithdrawalSuccess.observe(viewLifecycleOwner) { setIsWithdrawalSuccess(it) }

        // 구독권 기간 만료되었는지 관찰
        userViewModel.subscription.observe(viewLifecycleOwner) {


           when(userViewModel.isExpiredSubscription()){

               //구독권 기간 지났을 때
               true -> {

                   // 서버에 구독권 삭제, 쉐어드에 구독권 데이터 삭제
                   userViewModel.deleteSubscription()

                   // 구독권 갱신 다이얼로그 띄우기
                   setSubscriptionCustomDialog()

               }

               //구독권 기간 안 지났을 때
               else -> {}

           }

        }

        userViewModel.isVisibleDueDateText.observe(viewLifecycleOwner) {

            if (it) {

                binding.textViewDueDate.visibility = View.VISIBLE

            } else {

                binding.textViewDueDate.visibility = View.GONE

            }

        }

    } // setObserve()


    // 모든 메뉴 아이템의 체크 상태를 false로 설정하는 메서드
    private fun setAllMenuFalse(){

        for (i in 0 until binding.navigationView.menu.size()) {

            val item = binding.navigationView.menu.getItem(i)

            item.isChecked = false

            //소셜로그인이면 비밀번호 변경 메뉴 안보이게 하기
            when(userViewModel.loginType.value){

                "kakao", "naver", "google" -> {

                    if (item.toString() == getString(R.string.passwordChange)) {

                        item.isVisible = false

                    }

                }

            }

        }

    } // setAllMenuFalse()





    // 로그아웃 버튼 클릭 상태에 대한 처리
    private fun setIsLogoutButtonClick(it:Boolean){

        if(it){

            // 로그아웃 버튼 클릭 감지하면 로그인 프래그먼트로 이동 후 로그아웃 false값으로 변경

            this.findNavController().navigate(R.id.action_userFragment_to_loginFragment)

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

    } // setIsProgressButtonClick()


    // 다이얼로그 내의 회원탈퇴 클릭 상태에 대한 처리
    private fun setIsWithdrawalButtonClick(it: Boolean){

        if(it){

            // 인터넷 연결 x
            if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                setNetworkCustomDialog()

            // 인터넷 연결 o
            } else {

                userViewModel.withdrawal()
                userViewModel.setIsWithdrawalButtonClick(false)

            }

        }

    } // setIsWithdrawalButtonClick()


    // 회원탈퇴 성공 여부에 대한 처리
    private fun setIsWithdrawalSuccess(it: String){

        when(it){

            "success" -> {

                val bundle = bundleOf("loginType" to userViewModel.loginType.value)

                // 로그인 프래그먼트로 이동 후 회원탈퇴값 false값으로 변경
                this.findNavController().navigate(R.id.action_userFragment_to_loginFragment, bundle)


                userViewModel.setIsWithdrawalButtonClick(false)

                //바텀 네비게이션 GONE 처리
                (activity as MainActivity).goneBottomNavi()

                //바텀네비게이션 아이템클릭 초기화
//                (activity as MainActivity).setNaviItem(0)

                Toast.makeText(requireActivity(), getString(R.string.successWithdrawal),Toast.LENGTH_SHORT).show()

            }

            else -> {

                Toast.makeText(requireContext(), getString(R.string.networkConnectionIsUnstable), Toast.LENGTH_SHORT).show()

            }

        }

    } // setIsWithdrawalSuccess()


    //drawerLayout에 체크된 메뉴아이템 관찰 대한 처리
    private fun setSelectedMenuItem(it : MenuItem){

        // 네비게이션 메뉴 선택 효과 주기
        // isChecked = true : 메뉴 텍스트 굵기가 굵어짐.
        it.isChecked = true

        when(it.toString()){

            getString(R.string.profileEdit)  -> {

                //프로필 수정 프래그먼트로 이동
                this.findNavController().navigate(R.id.action_userFragment_to_userEditFragment)

                (activity as MainActivity).goneBottomNavi()

            }

           getString(R.string.passwordChange)   -> {

                //비밀번호 변경 프래그먼트로 이동
               val bundle = bundleOf("startingPoint" to "userFragment")

               this.findNavController().navigate(R.id.action_userFragment_to_findPasswordFragment,bundle)

               (activity as MainActivity).goneBottomNavi()

            }

            getString(R.string.logout)   -> {

                //로그아웃 다이얼로그
                setCustomDialog(getString(R.string.logout),getString(R.string.logoutDialogContent))

                customDialogBinding.textViewButtonOk.setTextColor(ContextCompat.getColor(requireContext(), R.color.personal))

            }

            getString(R.string.withdrawal)  -> {

                //회원탈퇴 진행 다이얼로그
                if(userViewModel.subscription.value != ""){ //유저정보에 구독권 기간이 존재하면

                    setCustomDialog(getString(R.string.progress),getString(R.string.progressDialogContent))

                    customDialogBinding.textViewButtonOk.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))

                }else{ //구독권 기간이 존재하지 않으면

                    userViewModel.setIsProgressButtonClick(true)

                }

            }

            else -> {}

        }

    } // setSelectedMenuItem()


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

        customDialogBinding.textViewContent.text = content
        customDialogBinding.textViewButtonOk.text = buttonOkText

        dialog.show()

        customDialogBinding.textViewButtonOk.setOnClickListener {
            
            userViewModel.setOnDialogOkButtonClick(buttonOkText)
            dialog.dismiss()
            
        }

        customDialogBinding.textViewCancel.setOnClickListener {

//            setAllMenuFalse()
            dialog.dismiss()

        }

    } // setCustomDialog()


    // DrawerLayout을 닫는 메서드 추가
    private fun closeDrawerIfNeeded() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {

            // 뒤로가기 버튼을 누를 때 DrawerLayout을 닫습니다.
            binding.drawerLayout.closeDrawer(GravityCompat.END)

        } else {

            // DrawerLayout 닫혀 있는경우엔 finish()
            (activity as MainActivity).finish()

        }

    } // closeDrawerIfNeeded()


    //프로필이미지뷰 셋
    private fun setCircleImageView(){

        Glide.with(this)
            //baseurl+쉐어드의 이미지경로
            .load(userViewModel.profileImagePath.value)
            .circleCrop()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .thumbnail(Glide.with(this).load(R.raw.loading))
            .into(binding.imageViewUserProfile)

    } // setCircleImageView()


    //커스텀 다이얼로그 띄우기
    private fun setNetworkCustomDialog(){

        // 부모가 있는지 확인하고, 있다면 부모에서 제거
        customNetworkDialogBinding.root.parent?.let {

            (it as ViewGroup).removeView(customNetworkDialogBinding.root)

        }

        //다이얼로그 생성
        val networkDialog = AlertDialog.Builder(requireContext())
            .setView(customNetworkDialogBinding.root)
            .setCancelable(true)
            .create()

        //뒷배경 투명으로 바꿔서 둥근모서리 보이게
        networkDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customNetworkDialogBinding.textViewButtonOk.setOnClickListener {

            if (dialog.isShowing) {

                dialog.dismiss()

            }

            networkDialog.dismiss()

        }

        networkDialog.setOnCancelListener {

            if (dialog.isShowing) {

                dialog.dismiss()

            }

            networkDialog.dismiss()

        }

        networkDialog.show()

    } // setNetworkCustomDialog()


    // 구독권 만료 안내 다이얼로그 띄우기
    private fun setSubscriptionCustomDialog(){

        // 부모가 있는지 확인하고, 있다면 부모에서 제거
        customSubscriptionDialogBinding.root.parent?.let {

            (it as ViewGroup).removeView(customSubscriptionDialogBinding.root)

        }

        //다이얼로그 생성
        val subscriptionDialog = AlertDialog.Builder(requireContext())
            .setView(customSubscriptionDialogBinding.root)
            .setCancelable(true)
            .create()

        //뒷배경 투명으로 바꿔서 둥근모서리 보이게
        subscriptionDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customSubscriptionDialogBinding.textViewButtonOk.text = getString(R.string.subscriptionDialogButtonOk)
        customSubscriptionDialogBinding.textViewButtonOk.setTextColor(ContextCompat.getColor(requireContext(), R.color.personal))
        customSubscriptionDialogBinding.textViewContent.text = getString(R.string.subscriptionDialogContent)

        customSubscriptionDialogBinding.textViewButtonOk.setOnClickListener {

            //페이 프래그먼트로 이동
            this.findNavController().navigate(R.id.action_userFragment_to_payFragment)
            (activity as MainActivity).goneBottomNavi()

            subscriptionDialog.dismiss()

        }

        customSubscriptionDialogBinding.textViewCancel.setOnClickListener {

            subscriptionDialog.dismiss()
        }

        subscriptionDialog.show()

    } // setNetworkCustomDialog()


    // 뒤로가기 눌렀을때
    private fun setOnBackPressed() {

        callback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                closeDrawerIfNeeded()

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(this,callback)

    } // onBackPressed()


}