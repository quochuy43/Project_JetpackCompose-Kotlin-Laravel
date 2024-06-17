package com.example.project_appmovie.login_signup_view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.project_appmovie.database.account.AccountViewModel
import com.example.project_appmovie.R
import com.example.project_appmovie.dialog.CustomDialogUISendEmail
import com.example.project_appmovie.dialog.CustomDialogUISendOTP
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, viewModel: AccountViewModel) {

    val context = LocalContext.current

    // Khai bao cac bien ra vi ban dau la chua viet len duoc
    // Để lưu trữ giá trị trạng thái, mutableStateOf chp phép thay đổi giá trị
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    // Biến ni sử dụng cho text mật khẩu
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    // Quen mat khau
    var openAlertSendOTP = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painterResource(id = R.drawable.login_1),
            contentDescription = "Login background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize())
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(painter = painterResource(id = R.drawable.login_2), contentDescription = "Login Image",
            modifier = Modifier.size(150.dp))
        Text(text = "Welcome", fontWeight = FontWeight.Bold, fontSize = 27.sp, color = Color.White)

        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Login to your account", fontSize = 20.sp, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))
//      Cai ni la hop Input    TextField(value = "", onValueChange = {})
        OutlinedTextField(value = email, onValueChange = {email = it}, label = {
            Text(text = "Email Address", fontSize = 20.sp, color = Color.White)
        },
            textStyle = TextStyle(fontSize = 20.sp, color = Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            trailingIcon = {
                TextButton(onClick = {
                    isPasswordVisible = !isPasswordVisible
                }) {
                    Text(text = if (isPasswordVisible) "Hide" else "Show", color = Color.White, fontSize = 12.5.sp)
                }
            },
            value = password, onValueChange = {password = it},
            label = { Text(text = "Password", fontSize = 20.sp, color = Color.White) },
            textStyle = TextStyle(fontSize = 20.sp, color = Color.White),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Go
            )
        )

        Spacer(modifier = Modifier.height(20.dp))
        // Ni la lay ra tin nhan tu tim kiem Credential trong log a
        Button(onClick = {
            viewModel.viewModelScope.launch {
                val isLoginSuccessful = viewModel.login(email, password)
                if (isLoginSuccessful) {
                    navController.navigate("started")
                }
                else {
                    Toast.makeText(context, "Incorrect email or password! Please try again!", Toast.LENGTH_SHORT).show()
                }
            }
        },
            shape = CircleShape,
            modifier = Modifier
                .width(280.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color.White)) {
            Text(text = "LOGIN", fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Forgot Password?",
            style = TextStyle(fontStyle = FontStyle.Italic),
            fontSize = 20.sp,
            color = Color.Red,
            modifier = Modifier.clickable {
                openAlertSendOTP.value = true
            }
        )
        if (openAlertSendOTP.value) {
            CustomDialogUISendOTP(openAlertSendOTP, viewModel)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Or sign in with", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(20.dp))
        Row (
            modifier = Modifier.fillMaxWidth(), // cos thee cach ra .padding
            horizontalArrangement = Arrangement.SpaceEvenly
        )
        {
            Image(painter = painterResource(id = R.drawable.login_3), contentDescription = "Facebook",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        // Facebook Code
                    }
            )
            Image(painter = painterResource(id = R.drawable.login_4), contentDescription = "Google",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        // Google Code
                    }
            )
            Image(painter = painterResource(id = R.drawable.login_5), contentDescription = "Tik tok",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        // Tik tok Code
                    }
            )
        }

        // Tạo dấu gạch ngang
        Divider(
            color = Color.Black.copy(alpha = 0.3f),
            thickness = 2.5.dp,
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(horizontal = 60.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))
        Row (horizontalArrangement = Arrangement.SpaceEvenly){
            Text(text = "Don't have an account?", fontSize = 20.sp, color = Color.Black)
            Text(text = " SIGN UP", fontSize = 20.sp, color = Color.Blue,
                modifier = Modifier.clickable {
                    navController.navigate("signup")
                })
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}


