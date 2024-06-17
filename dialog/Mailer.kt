package com.example.project_appmovie.dialog

import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object Mailer {
    // Tạo mã OTP ngẫu nhiên
    fun genereateOTP(): String {
        return (100000..999999).random().toString()
    }

    suspend fun sendMail(from: String, to: String, subject: String, body: String, username: String, password: String): Boolean {
        val properties = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
        }

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        return try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(from))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
                setSubject(subject)
                setText(body)
            }
            Transport.send(message)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Phuong thuc gui OTP
    suspend fun sendOTP(to: String, otp: String, adminEmail: String, adminPassword: String): Boolean {
        val subject = "Your OTP Code"
        val body = "Your OTP code is $otp"
        val from = adminEmail

        return sendMail(from, to, subject, body, adminEmail, adminPassword)
    }
}