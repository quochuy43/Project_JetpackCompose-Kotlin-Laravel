package com.example.project_appmovie.UI_View

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.rounded.OndemandVideo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign
import com.example.project_appmovie.R
import com.example.project_appmovie.ui.theme.orange
import com.example.project_appmovie.ui.theme.redd

@Composable
fun StartedScreen(navController: NavHostController) {

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.started_1),
            contentDescription = "Started background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize())
    }

    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hình ảnh vô trước
        Image(
            painter = painterResource(id = R.drawable.started_2),
            contentDescription = "Introduce film",
            modifier = Modifier
                .size(550.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "LET'S DISCOVER YOUR",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = orange,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "FAVOURITE MOVIE",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = orange,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        )

        Text(
            text = "Are you looking for romantic dramas, blockbuster action or funny stories?",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 25.dp)
        )

        Text(
            text = "We have it all!",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        ElevatedButton(
            onClick = {
                navController.navigate("main")
            },
            colors = ButtonDefaults.buttonColors(redd),
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            shape = CircleShape
        ) {
            Text(
                text = "GET STARTED",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(18.dp))

            Icon(
                imageVector = Icons.Default.ArrowCircleRight,
                contentDescription = "started",
                tint = Color.White
            )
        }
    }
}