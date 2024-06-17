package com.example.project_appmovie.movieList.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_appmovie.R
import com.example.project_appmovie.ui.theme.Purple80
import com.example.project_appmovie.ui.theme.dark_blue
import com.example.project_appmovie.ui.theme.dark_red
import com.example.project_appmovie.ui.theme.dark_yellow
import com.example.project_appmovie.ui.theme.redd
import com.example.project_appmovie.ui.theme.white_yellow

@Composable
fun ActorCard(
    modifier: Modifier = Modifier,
    nameActor: String,
    imageActor: Int,
    ageActor: Int,
    countryActor: String,
    onclick: () -> Unit
) {
    Box (
        modifier = Modifier
            .size(150.dp)
            .clickable { onclick() }
            .clip(RoundedCornerShape(25.dp))
            .background(white_yellow)
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .padding(5.dp)
                    .clip(CircleShape) // Đặt hình tròn cho hình ảnh
                    .background(dark_yellow), // Màu nền mặc định hoặc tùy chọn khác
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imageActor),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape) // Đặt hình tròn cho hình ảnh
                )
            }

            Text(
                text = nameActor,
                fontSize = 12.9.sp,
                color = dark_blue,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Age: $ageActor",
                fontSize = 12.9.sp,
                color = dark_red
            )
            Text(
                text = countryActor,
                fontSize = 12.9.sp,
                color = dark_red
            )
        }
    }
}
