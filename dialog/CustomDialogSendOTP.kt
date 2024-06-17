package com.example.project_appmovie.dialog

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.project_appmovie.database.HomeViewModel
import com.example.project_appmovie.database.account.AccountViewModel
import com.example.project_appmovie.ui.theme.dark_blue
import com.example.project_appmovie.ui.theme.dark_yellow
import com.example.project_appmovie.ui.theme.orange
import com.example.project_appmovie.ui.theme.redd
import com.example.project_appmovie.ui.theme.white_yellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CustomDialogUISendOTP(
    openDialogBox: MutableState<Boolean>,
    viewModel: AccountViewModel) {
    Dialog(onDismissRequest = { openDialogBox.value = false }) {
        CustomUI(openDialogBox, viewModel)
    }
}

@Composable
private fun CustomUI(
    openDialog: MutableState<Boolean>,
    viewModel: AccountViewModel
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(1) }
    var showOtpSentMessage by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.background(white_yellow)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                when (step) {
                    1 -> {
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = {
                            viewModel.sendOTP(email)
                            showOtpSentMessage = true
                        }) {
                            Text("Send OTP")
                        }
                        if (showOtpSentMessage) {
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "OTP has been sent to your email.",
                                color = Color.Black)
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(onClick = {
                                step = 2 // Chuyển sang bước 2 để xác nhận OTP và cập nhật mật khẩu mới
                            }) {
                                Text("Continue")
                            }
                        }
                    }
                    2 -> {
                        TextField(
                            value = otp,
                            onValueChange = { otp = it },
                            label = { Text("OTP") }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        TextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("New Password") }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = {
                            viewModel.verifyOTPAndUpdatePass(email, otp, newPassword) { success ->
                                if (success) {
                                    Toast.makeText(context, "Password updated successfully, please login again!", Toast.LENGTH_SHORT).show()
                                    openDialog.value = false
                                } else {
                                    Toast.makeText(context, "Invalid OTP, please try again.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }) {
                            Text("Verify OTP and Update Password")
                        }
                    }
                }
            }
        }
    }
}