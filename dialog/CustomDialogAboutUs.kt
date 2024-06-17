package com.example.project_appmovie.dialog

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.project_appmovie.R
import com.example.project_appmovie.ui.theme.dark_blue
import com.example.project_appmovie.ui.theme.dark_yellow
import com.example.project_appmovie.ui.theme.redd
import com.example.project_appmovie.ui.theme.white_yellow

@Composable
fun CustomDialogUIAboutUs(openDialogBox: MutableState<Boolean>) {
    Dialog(onDismissRequest = { openDialogBox.value = false }) {
        CustomUI(openDialogBox)
    }
}

@Composable
private fun CustomUI(openDialog: MutableState<Boolean>) {

    // Thanh cuộn
    val scrollState = rememberScrollState()

    // Lưu tru bien context
    val context = LocalContext.current

    Surface(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .padding(16.dp),
        color = white_yellow
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "About Us",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = dark_yellow,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Satisfy your passion for cinema with the Movie Review app! Providing a huge movie store, a variety of genres and smart features, Movie Review will bring you hours of great entertainment.",
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hình ảnh giới thiệu
            Image(
                painter = painterResource(id = R.drawable.logo), // Thay bằng hình ảnh của bạn
                contentDescription = "Company Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp)
                    .clip(shape = MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Thành tựu và chứng nhận
            Text(
                text = "Application Manager",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = dark_yellow,
            )
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .padding(5.dp)
                    .border(1.dp, white_yellow, CircleShape)
                    .clip(CircleShape), // Đặt hình tròn cho hình ảnh
                contentAlignment = Alignment.Center
            ) {
                
                Image(
                    painter = painterResource(id = R.drawable.profile_1),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                
            }

            Text(
                text = "Mr. Huy is a dynamic and creative application manager. He always has fresh ideas to improve the app and bring the best experience to users. He is also an avid learner and is always up to date with the latest knowledge and skills in the field of application development",
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Liên hệ
            Text(
                text = "Contact Us",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = dark_yellow,
            )
            Text(
                text = "Contact us by email: huylvq.22ite@vku.udn.vn or phone number: 0766688287",
                fontSize = 16.sp,
                color = Color.Black
            )
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            )
            {
                val link_fb = "https://www.facebook.com/profile.php?id=100031398211869"
                val link_tiktok = "https://www.tiktok.com/@wuvwy0403"
                val link_insta = "https://www.instagram.com/qhuy_43/?hl=en"
                val intent_fb = Intent(Intent.ACTION_VIEW, Uri.parse(link_fb))
                val intent_tiktok = Intent(Intent.ACTION_VIEW, Uri.parse(link_tiktok))
                val intent_insta = Intent(Intent.ACTION_VIEW, Uri.parse(link_insta))

                Image(painter = painterResource(id = R.drawable.login_3), contentDescription = "Facebook",
                    modifier = Modifier
                        .size(45.dp)
                        .clickable {
                            context.startActivity(intent_fb)
                        }
                )
                Image(painter = painterResource(id = R.drawable.login_5), contentDescription = "Google",
                    modifier = Modifier
                        .size(45.dp)
                        .clickable {
                            context.startActivity(intent_tiktok)
                        }
                )
                Image(painter = painterResource(id = R.drawable.login_6), contentDescription = "Tik tok",
                    modifier = Modifier
                        .size(45.dp)
                        .clickable {
                            context.startActivity(intent_insta)
                        }
                )
            }
        }
    }
}
