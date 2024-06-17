package com.example.project_appmovie.dialog

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.project_appmovie.database.HomeViewModel
import com.example.project_appmovie.ui.theme.dark_blue
import com.example.project_appmovie.ui.theme.dark_yellow
import com.example.project_appmovie.ui.theme.white_yellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CustomDialogUISendEmail(openDialogBox: MutableState<Boolean>) {
    Dialog(onDismissRequest = { openDialogBox.value = false }) {
        CustomUI(openDialogBox)
    }
}

@Composable
private fun CustomUI(openDialog: MutableState<Boolean>) {
    var emailContent by remember { mutableStateOf("") }

    // Khai báo biến để lay thông tin người dùng
    val homeViewModel: HomeViewModel = hiltViewModel()
    val userEmail by homeViewModel.userEmail.observeAsState("Unknown Email")

    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.background(white_yellow)
        ) {
            Column(Modifier.padding(6.dp)) {
                Text(
                    text = "PLEASE SEND AN EMAIL",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = dark_yellow,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "If you have questions, write down here the questions that you need answered. We will get back to you as soon as possible!",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = dark_blue,
                    fontSize = 16.sp
                )

                OutlinedTextField(
                    value = emailContent,
                    onValueChange = {emailContent = it},
                    label = {
                        Text(text = "Write your questions", fontSize = 16.sp, color = dark_blue)
                    },
                    textStyle = TextStyle(fontSize = 16.sp, color = dark_blue),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            TextButton(
                onClick = {
                    val fromEmail = userEmail
                    val toEmail = "huylvq.22ite@vku.udn.vn"
                    val subject = "Email from $userEmail"
                    val body = emailContent
                    val username = userEmail
                    val password = "zyfj blue fffv ejmx"

                    CoroutineScope(Dispatchers.IO).launch {
                        val result = Mailer.sendMail(fromEmail, toEmail, subject, body, username, password)
                        withContext(Dispatchers.Main) {
                            if (result) {
                                Toast.makeText(context, "Send email successful!", Toast.LENGTH_SHORT).show()
                                emailContent = ""
                            } else {
                                Toast.makeText(context, "Send email error!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .background(dark_blue)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Send", fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}