package com.example.project_appmovie.database.account

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.project_appmovie.MyDatabase
import com.example.project_appmovie.dialog.Mailer
import com.example.project_appmovie.movieList.presentation.base64ToBitMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val myDatabase: MyDatabase,
    private val mailer: Mailer,
    application: Application
): AndroidViewModel(application) {

    // accountDAO to communicate with DB
    val accountDAO = myDatabase.getAccountDao()

    val accountList: LiveData<List<Account>> = accountDAO.getAllAccount()

    // Thuoc tinh de luu ma OTP va email
    private var storedOTP: String = ""
    private var otpEmail: String = ""

    // Check email exist, when need query to database, use suspend function
    suspend fun checkEmailExist(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            val existingAccount = accountDAO.getAccountByEmail(email)
            existingAccount != null
        }
    }

    // Add account
    fun addAccount(fullName: String, age: Int, phoneNumber: Int, email: String, password: String) {
        viewModelScope.launch (Dispatchers.IO){
            accountDAO.addAccount(Account(fullName = fullName, age = age, phoneNumber = phoneNumber, email = email, password = password))
        }
    }

    // Delete Account
    fun deleteAccount(id: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            accountDAO.deleteAccount(id)
        }
    }

    // Login, sử dụng SharedPreferences để lưu thông tin tài khoản người dùng
    suspend fun login(email: String, pass: String): Boolean {
        var isLoginSuccessful = false
        withContext(Dispatchers.IO) {
            val account = accountDAO.getAccountByEmail(email)
            if (account != null && account.password == pass) {
                isLoginSuccessful = true
                saveUserInfo(account)
            }
        }
        return isLoginSuccessful
    }

    private fun saveUserInfo(account: Account) {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putInt("user_id", account.id)
            putString("user_email", account.email)
            putString("user_pass", account.password)
            putString("user_name", account.fullName)
            putInt("user_phone", account.phoneNumber)
            putInt("user_age", account.age)
            apply()
        }
    }

//     Check email
    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    // Đăng xuất
    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            val sharedPreferences = getApplication<Application>().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
        }
    }

    // Tai anh len
    private val _userAvatar = MutableStateFlow<Bitmap?>(null)
    val userAvatar: StateFlow<Bitmap?> = _userAvatar

    suspend fun loadUserAvatar(email: String) {
        withContext(Dispatchers.IO) {
            val account = accountDAO.getAccountByEmail(email)
            account?.let {
                _userAvatar.value = it.avatar?.let { base64ToBitMap(it) }
            }
        }
    }

    // Luu Avatar
    suspend fun updateAccountAvatar(email: String, avatar: String) {
        withContext(Dispatchers.IO) {
            val account = accountDAO.getAccountByEmail(email)
            account?.let {
                it.avatar = avatar
                accountDAO.updateAccount(it)
                // Cập nhật ảnh trong MutableState
                _userAvatar.value = base64ToBitMap(avatar)
            }
        }
    }

    // Cập nhật thông tin người dùng
    fun updateUserInfo(name: String, age: Int, phoneNumber: Int, pass: String, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val account = accountDAO.getAccountByEmail(email)
            account?.let {
                it.fullName = name
                it.age = age
                it.password = pass
                it.phoneNumber = phoneNumber
                accountDAO.updateAccount(it)
                Log.d("UpdateInfo", "Updated account: $it")
            }
        }
    }

    // Phuong thuc OTP
    fun sendOTP(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val emailExists = withContext(Dispatchers.IO) {
                checkEmailExist(email)
            }
            if (emailExists) {
                val generatedOTP = mailer.genereateOTP()
                storedOTP = generatedOTP
                otpEmail = email
                val result = mailer.sendOTP(
                    to = email,
                    otp = generatedOTP,
                    adminEmail = "huylvq.22ite@vku.udn.vn",  // Email của admin
                    adminPassword = "hongvinh6543"  // Mật khẩu của admin
                )
                if (result) {
                    Log.d("SendOtp", "OTP sent successfully, OTP: $storedOTP")
                } else {
                    Log.d("SendOtp", "Failed to send OTP")
                }
            } else {
                Log.d("SendOtp", "Email does not exist")
            }
        }
    }

    // Xac minh otp va cap nhat mat khau moi
    fun verifyOTPAndUpdatePass(email: String, inputOTP: String, newPass: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (inputOTP == storedOTP && email == otpEmail) {
                val account = accountDAO.getAccountByEmail(email)
                if (account != null) {
                    account.password = newPass
                    accountDAO.updateAccount(account)
                    withContext(Dispatchers.Main) {
                        onResult(true)
                        Log.d("UpdatePassword", "Password updated successfully")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onResult(false)
                        Log.d("UpdatePassword", "Account not found")
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    onResult(false)
                    Log.d("UpdatePassword", "Invalid OTP or email mismatch")
                }
            }
        }
    }
}