package com.example.project_appmovie.dialog

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_appmovie.database.HomeViewModel
import com.example.project_appmovie.database.comments.CommentViewModel
import com.example.project_appmovie.details.movie.DetailsViewModelMovie
import com.example.project_appmovie.ui.theme.dark_blue
import com.example.project_appmovie.ui.theme.redd
import com.example.project_appmovie.ui.theme.white_yellow
import kotlinx.coroutines.launch
import com.example.project_appmovie.R
import com.example.project_appmovie.database.account.AccountViewModel

@Composable
fun CustomDialogUIComment(openDialogBox: MutableState<Boolean>) {
    Dialog(onDismissRequest = { openDialogBox.value = false }) {
        val commentViewModel: CommentViewModel = hiltViewModel()
        CustomUI(openDialogBox, commentViewModel)
    }
}

@Composable
private fun CustomUI(openDialog: MutableState<Boolean>, commentViewModel: CommentViewModel) {

    var opinion by remember { mutableStateOf("") }

    var context = LocalContext.current

    // Lay du lieu phim chi tiet
    val detailsViewModel = hiltViewModel<DetailsViewModelMovie>()
    val detailState = detailsViewModel.detailState.collectAsState().value
    val movie_id = detailState.movie?.id

    // Lay du lieu nguoi dung
    val homeViewModel: HomeViewModel = hiltViewModel()
    val username by homeViewModel.userName.observeAsState("Unknown User")
    val userId by homeViewModel.userId.observeAsState(-1)


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
                    text = "Please give any comments",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = dark_blue,
                    fontSize = 20.sp,
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(dark_blue, redd)
                        )
                    )
                )

                Text(
                    text = "Have you seen this movie before? If you've already seen it, please write a comment here!",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = dark_blue,
                    fontSize = 16.sp
                )

                OutlinedTextField(
                    value = opinion,
                    onValueChange = {opinion = it},
                    label = {
                        Text(text = "Write your opinion", fontSize = 16.sp, color = dark_blue)
                    },
                    textStyle = TextStyle(fontSize = 16.sp, color = dark_blue),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            TextButton(
                onClick = {
                    Log.d("CustomUI", "Comment button clicked with values: movie_id=$movie_id, username=$username, opinion=$opinion")
                    if (movie_id != null && username.isNotEmpty() && opinion.isNotEmpty()) {
                        commentViewModel.viewModelScope.launch {
                            try {
                                commentViewModel.addComment(movie_id, userId, username, opinion)
                                openDialog.value = false // Đóng hộp thoại sau khi thêm bình luận
                                Log.d("CustomUI", "Comment added successfully")
                            } catch (e: Exception) {
                                Log.e("CustomUI", "Failed to add comment", e)
                            }
                        }
                    } else {
                        Log.e("CustomUI", "Invalid input values: movie_id=$movie_id, username=$username, opinion=$opinion")
                    }
                    Toast.makeText(context, "Thanks for your comment!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .background(dark_blue)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "COMMENT", fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}