package com.example.project_appmovie.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.project_appmovie.ui.theme.dark_red
import com.example.project_appmovie.ui.theme.dark_yellow
import com.example.project_appmovie.ui.theme.white_yellow

@Composable
fun CustomDialogUISecurity(openDialogBox: MutableState<Boolean>) {
    Dialog(onDismissRequest = { openDialogBox.value = false }) {
        SecurityAndPrivacyUI(openDialogBox)
    }
}

@Composable
private fun SecurityAndPrivacyUI(openDialog: MutableState<Boolean>) {

    val scrollState = rememberScrollState()

    Surface(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = white_yellow
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Căn giữa các thành phần theo chiều ngang
        ) {
            // Tiêu đề và mô tả ngắn
            Text(
                text = "Security and Privacy",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = dark_yellow,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Căn giữa tiêu đề
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "1. We take your security and privacy seriously.",
                fontSize = 16.sp,
                color = dark_red,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Căn giữa mô tả ngắn
            )
            Spacer(modifier = Modifier.height(6.5.dp))

            Text(
                text = "2. We are committed to protecting your personal data and complying with privacy regulations.",
                fontSize = 16.sp,
                color = dark_red,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Căn giữa nội dung
            )
            Spacer(modifier = Modifier.height(6.5.dp))

            Text(
                text = "3. We collect data only necessary to provide and improve our services. Your data will not be shared with third parties without your consent.",
                fontSize = 16.sp,
                color = dark_red,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Căn giữa nội dung
            )
            Spacer(modifier = Modifier.height(6.5.dp))

            Text(
                text = "4. We use advanced security measures to protect your data from unauthorized access, loss, or disclosure.",
                fontSize = 16.sp,
                color = dark_red,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Căn giữa nội dung
            )
            Spacer(modifier = Modifier.height(6.5.dp))

            Text(
                text = "5. You have the right to access, rectify and delete your personal data. If you have any questions about your privacy, contact us.",
                fontSize = 16.sp,
                color = dark_red,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Căn giữa nội dung
            )
            Spacer(modifier = Modifier.height(6.5.dp))

            Text(
                text = "6. If you have any questions about our privacy policy, please contact us by email: huylvq.22ite@vku.udn.vn or phone number: 0766688287",
                fontSize = 16.sp,
                color = dark_red,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Căn giữa nội dung
            )
        }
    }
}
