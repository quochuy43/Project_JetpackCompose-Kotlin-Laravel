package com.example.project_appmovie.login_signup_view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.project_appmovie.ui.theme.dark_blue
import com.example.project_appmovie.ui.theme.redd
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(navController: NavHostController, viewModel: AccountViewModel) {

    val context = LocalContext.current

    var fullName by remember {
        mutableStateOf("")
    }
    var age by remember {
        mutableStateOf("")
    }
    var phoneNumber by remember {
        mutableStateOf("")
    }

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

    // Hộp thoại thông báo

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.signup_1),
            contentDescription = "Login background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize())
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row (horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
            Column {
                Text(
                    text = "SIGN UP",
                    fontWeight = FontWeight.Bold,
                    fontSize = 50.sp,
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(redd, dark_blue)
                        )
                    )
                )
                Text(
                    text = "Create a new account",
                    fontSize = 20.sp,
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(redd, dark_blue)
                        )
                    )
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Box(
                modifier = Modifier
                    .size(165.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = fullName, onValueChange = {fullName = it}, label = {
            Text(text = "Full Name", fontSize = 20.sp, color = Color.Black)
        },
            textStyle = TextStyle(fontSize = 20.sp, color = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = age, onValueChange = {age = it}, label = {
            Text(text = "Age", fontSize = 20.sp, color = Color.Black)
        },
            textStyle = TextStyle(fontSize = 20.sp, color = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = phoneNumber, onValueChange = {phoneNumber = it}, label = {
            Text(text = "Phone Number", fontSize = 20.sp, color = Color.Black)
        },
            textStyle = TextStyle(fontSize = 20.sp, color = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))
//      Cai ni la hop Input    TextField(value = "", onValueChange = {})
        OutlinedTextField(value = email, onValueChange = {email = it}, label = {
            Text(text = "Email Address", fontSize = 20.sp, color = Color.Black)
        },
            textStyle = TextStyle(fontSize = 20.sp, color = Color.Black)
        )


        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            trailingIcon = {
                TextButton(onClick = {
                    isPasswordVisible = !isPasswordVisible
                }) {
                    Text(text = if (isPasswordVisible) "Hide" else "Show", color = Color.Black, fontSize = 12.5.sp)
                }
            },
            value = password, onValueChange = {password = it},
            label = { Text(text = "Password", fontSize = 20.sp, color = Color.Black) },
            textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Go
            )
        )

        Spacer(modifier = Modifier.height(20.dp))
        Row() {
            val checkAgreement = remember {
                mutableStateOf(false)
            }

            Checkbox(
                checked = checkAgreement.value,
                onCheckedChange = { checkAgreement.value = it }
            )

            Text(text = "I have agreed to the terms in the app", fontSize = 17.sp, modifier = Modifier.padding(top = 12.dp), fontStyle = FontStyle.Italic)
        }
        // Ni la lay ra tin nhan tu tim kiem Credential trong log a

        Button(onClick = {
            viewModel.viewModelScope.launch {
                // Check valid email
                val isEmailValid = viewModel.isValidEmail(email)
                if (!isEmailValid) {
                    Toast.makeText(context, "Invalid email address! Please enter a valid email.", Toast.LENGTH_SHORT).show()
                    // Kiểu thông báo lỗi rồi không tiếp tục các bước tiêp theo
                    return@launch
                }
                else {
                    // Check exist email
                    val isEmailExist = viewModel.checkEmailExist(email)
                    if (!isEmailExist) {
                        val ageString = age.toString()
                        val phoneString = phoneNumber.toString()
                        viewModel.addAccount(fullName, age.toInt(), phoneNumber.toInt(), email, password)
                        Toast.makeText(context, "You have successfully registered!", Toast.LENGTH_SHORT).show()
                        fullName = ""
                        age = 0.toString()
                        phoneNumber = ""
                        email = ""
                        password = ""
                    } else {
                        Toast.makeText(
                            context,
                            "Email already exist! Please try again!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        },
            modifier = Modifier
                .width(280.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(dark_blue)) {
            Text(text = "REGISTER", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }

        // Tạo dấu gạch ngang
        Divider(
            color = Color.Black.copy(alpha = 0.3f),
            thickness = 2.5.dp,
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(horizontal = 60.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
        Row (horizontalArrangement = Arrangement.SpaceEvenly){
            Text(text = "Already have an account?", fontSize = 20.sp, color = Color.Black)
            Text(text = " LOGIN", fontSize = 20.sp, color = Color.Blue ,modifier = Modifier.clickable {
                navController.navigate("login")
            })
        }
    }
}