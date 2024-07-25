package com.example.fitfit.network

import com.example.fitfit.data.Challenge
import com.example.fitfit.data.ChallengeResponse
import com.example.fitfit.data.KakaoPayLoad
import com.example.fitfit.data.PaymentApproveRequest
import com.example.fitfit.data.PaymentReadyRequest
import com.example.fitfit.data.PaymentReadyResponse
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.Rank
import com.example.fitfit.data.SplashResponse
import com.example.fitfit.data.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RetrofitInterface {

    // 유저 정보 불러오기 로그인,비밀번호 찾기
    @FormUrlEncoded
    @POST("userProcess.php")
    suspend fun selectUserData(

        @Field("id") id: String,
        @Field("password") password: String,
        @Field("nickname") nickname: String,
        @Field("mode") mode: String

    ): Response<User>


    // 회원가입
    @FormUrlEncoded
    @POST("userProcess.php")
    suspend fun signUp(

        @Field("id") id: String,
        @Field("password") password: String,
        @Field("nickname") nickname: String,
        @Field("mode") mode: String

    ): Response<User>


    // 회원 탈퇴
    @FormUrlEncoded
    @POST("userProcess.php")
    suspend fun withdrawal(

        @Field("id") id: String,
        @Field("mode") mode: String

    ): Response<User>


    // 유저 운동 정보 읽어오는 메서드
    @FormUrlEncoded
    @POST("userProcess.php")
    suspend fun selectUserExercise(

        @Field("id") id: String,
        @Field("mode") mode: String

    ): Response<SplashResponse>


    // 운동 정보를 insert 해주는 메서드
    @FormUrlEncoded
    @POST("exerciseProcess.php")
    suspend fun insertPoseExercise(

        @Field("id") id: String,
        @Field("category") category: String,
        @Field("exerciseName") exerciseName: String,
        @Field("exerciseCount") exerciseCount: Int,
        @Field("goalExerciseCount") goalExerciseCount: Int,
        @Field("date") date: Long,
        @Field("checkList") checkList: Int,
        @Field("mode") mode: String

    ): Response<PoseExercise>


    // 운동 정보 update 해주는 메서드
    @FormUrlEncoded
    @POST("exerciseProcess.php")
    suspend fun updatePoseExercise(

        @Field("id") id: String,
        @Field("category") category: String,
        @Field("exerciseName") exerciseName: String,
        @Field("exerciseCount") exerciseCount: Int,
        @Field("goalExerciseCount") goalExerciseCount: Int,
        @Field("date") date: Long,
        @Field("checkList") checkList: Int,
        @Field("mode") mode: String

    ): Response<PoseExercise>

    //프로필 수정
    @Multipart
    @POST("profileEditProcess.php")
    suspend fun profileEdit(

        @Part image: MultipartBody.Part?,
        @Part("id") id: RequestBody,
        @Part("nickname") nickname: RequestBody,
        @Part("mode") mode: RequestBody


        ): Response<User>



    //이미지 수정 안했을 때 프로필 수정
    @FormUrlEncoded
    @POST("profileEditProcess.php")
    suspend fun profileEditWithoutImage(

        @Field("id") id: String,
        @Field("nickname") nickname: String,
        @Field("mode") mode: String

    ): Response<User>



    //내 이메일 인지 확인
    @FormUrlEncoded
    @POST("passwordResetProcess.php")
    suspend fun passwordResetProcess(

        @Field("id") id: String,
        @Field("password") password: String,
        @Field("mode") mode: String

    ): Response<User>




    //어레이리스트 전송
    @FormUrlEncoded
    @POST("exerciseProcess.php")
    suspend fun setMyCheckList(

        @Field("id") id: String?,
        @Field("checkList") checkList: String?,
        @Field("mode") mode: String?

    ): Response<SplashResponse>


    @FormUrlEncoded
    @POST("challengeProcess.php")
    suspend fun challengeJoin(

        @Field("id") id: String?,
        @Field("challengeName") challengeName: String?,
        @Field("mode") mode: String?

    ): Response<ChallengeResponse>?



    @FormUrlEncoded
    @POST("challengeProcess.php")
    suspend fun getMyChallengeList(

        @Field("id") id: String?,
        @Field("mode") mode: String?

    ): Response<ArrayList<Challenge>>


    // 카카오페이 요청
    @Headers("Authorization:SECRET_KEY DEVF70FE81BA55A6924A2361A24B570781466812")
    @POST("v1/payment/ready")
    suspend fun readyKakaoPay(

        @Body request: PaymentReadyRequest

    ): Response<PaymentReadyResponse>



    // 카카오페이 승인
    @Headers("Authorization:SECRET_KEY DEVF70FE81BA55A6924A2361A24B570781466812")
    @POST("v1/payment/approve")
    suspend fun approveKakaoPay(

        @Body paymentApproveRequest: PaymentApproveRequest

    ): Response<KakaoPayLoad>



    //랭킹 리스트 요청
    @FormUrlEncoded
    @POST("rankingProcess.php")
    suspend fun getRankingList(

        @Field("challengeName") challengeName: String?,
        @Field("mode") mode: String?,

        ): Response<ArrayList<Rank>>


    // 구독권 기간
    @FormUrlEncoded
    @POST("payProcess.php")
    suspend fun updateSubscription(

        @Field("id") id: String?,
        @Field("days") days: Int?

    ): Response<User>


    //소셜 로그인
    @FormUrlEncoded
    @POST(" socialLoginProcess.php")
    suspend fun socialLogin(

        @Field("id") id: String?,
        @Field("loginType") loginType: String?

    ): Response<User>

}