package com.example.fitfit.function

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class GmailSender : Authenticator() {

    // 보내는 사람 이메일과 비밀번호
    private val stringResource = StringResource.GmailSenderStringResource

    private val fromEmail = stringResource.fromEmail
    private val password = stringResource.password

    // 보내는 사람 계정 확인
    override fun getPasswordAuthentication(): PasswordAuthentication = PasswordAuthentication(fromEmail, password)
    // getPasswordAuthentication()


    // 메일 보내기
    fun sendEmail(toEmail: String,title : String,randomString : String) {

        CoroutineScope(Dispatchers.IO).launch {

            val props = Properties()
            props.setProperty("mail.transport.protocol", "smtp")
            props.setProperty("mail.host", "smtp.gmail.com")
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.port"] = "465"
            props["mail.smtp.socketFactory.port"] = "465"
            props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
            props["mail.smtp.socketFactory.fallback"] = "false"
            props.setProperty("mail.smtp.quitwait", "false")

            // 구글에서 지원하는 smtp 정보를 받아와 MimeMessage 객체에 전달
            val session = Session.getDefaultInstance(props, this@GmailSender)

            // 메시지 객체 만들기
            val message = MimeMessage(session)

            // 보내는 사람 설정
            message.sender = InternetAddress(fromEmail)

            // 받는 사람 설정
            message.addRecipient(Message.RecipientType.TO, InternetAddress(toEmail))

            // 이메일 제목
            message.subject = title

            // 이메일 내용
            message.setText(randomString)

            // 전송
            try {

                // 이메일 전송 코드
                Transport.send(message)

            } catch (e: AuthenticationFailedException) {

                // 앱 비밀번호가 틀린경우
                Log.e("TAG", "Authentication failed: ${e.message}")

            }

        }

    } // sendEmail()

}