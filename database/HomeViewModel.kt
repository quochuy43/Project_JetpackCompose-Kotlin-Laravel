package com.example.project_appmovie.database

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(context: Context): ViewModel() {
    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val _userId = MutableLiveData<Int>().apply { value = -1 }
    val userId: LiveData<Int> get() = _userId

    private val _userEmail = MutableLiveData<String>().apply { value = "Unknown Email" }
    val userEmail: LiveData<String> get() = _userEmail

    private val _userPass = MutableLiveData<String>().apply { value = "Unknown Pass" }
    val userPass: LiveData<String> get() = _userPass

    private val _userName = MutableLiveData<String>().apply { value = "Unknown Name" }
    val userName: LiveData<String> get() = _userName

    private val _userPhone = MutableLiveData<Int>().apply { value = 0 }
    val userPhone: LiveData<Int> get() = _userPhone

    private val _userAge = MutableLiveData<Int>().apply { value = 0 }
    val userAge: LiveData<Int> get() = _userAge

    private val sharedPreferencesListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        when (key) {
            "user_id" -> _userId.value = sharedPreferences.getInt(key, -1)
            "user_email" -> _userEmail.value = sharedPreferences.getString(key, "Unknown Email")
            "user_pass" -> _userPass.value = sharedPreferences.getString(key, "Unknown Pass")
            "user_name" -> _userName.value = sharedPreferences.getString(key, "Unknown Name")
            "user_phone" -> _userPhone.value = sharedPreferences.getInt(key, 0)
            "user_age" -> _userAge.value = sharedPreferences.getInt(key, 0)
        }
    }


    init {
        // Lắng nghe sự thay đổi trong SharedPreferences
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferencesListener)

        // Khởi tạo dữ liệu từ SharedPreferences
        _userId.value = sharedPreferences.getInt("user_id", -1)
        _userEmail.value = sharedPreferences.getString("user_email", "Unknown Email")
        _userPass.value = sharedPreferences.getString("user_pass", "Unknown Pass")
        _userName.value = sharedPreferences.getString("user_name", "Unknown Name")
        _userPhone.value = sharedPreferences.getInt("user_phone", 0)
        _userAge.value = sharedPreferences.getInt("user_age", 0)
    }

    // Hủy đăng ký lắng nghe SharedPreferences khi ViewModel bị hủy
    override fun onCleared() {
        super.onCleared()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferencesListener)
    }
}