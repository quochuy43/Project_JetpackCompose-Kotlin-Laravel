package com.example.project_appmovie.details.movie

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material.icons.rounded.InsertComment
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.project_appmovie.R
import com.example.project_appmovie.database.HomeViewModel
import com.example.project_appmovie.database.comments.Comment
import com.example.project_appmovie.database.comments.CommentViewModel
import com.example.project_appmovie.database.favourite_movies.FavouriteViewModel
import com.example.project_appmovie.dialog.CustomDialogUIComment
import com.example.project_appmovie.movieList.data.remote.MovieAPI
import com.example.project_appmovie.movieList.util.RatingBar
import com.example.project_appmovie.trailer.YouTubePlayer
import com.example.project_appmovie.ui.theme.Purple80
import com.example.project_appmovie.ui.theme.dark_yellow
import com.example.project_appmovie.ui.theme.light_back
import com.example.project_appmovie.ui.theme.orange
import com.example.project_appmovie.ui.theme.redd
import com.example.project_appmovie.ui.theme.white_yellow
import java.text.SimpleDateFormat
import java.util.Locale
@Composable
fun DetailScreenMovie(
    backStackEntry: NavBackStackEntry,
    commentViewModel: CommentViewModel,
    homeViewModel: HomeViewModel
) {
    val detailsViewModel = hiltViewModel<DetailsViewModelMovie>()
    val detailState = detailsViewModel.detailState.collectAsState().value

    val backDropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieAPI.IMAGE_BASE_URL + detailState.movie?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val posterImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieAPI.IMAGE_BASE_URL + detailState.movie?.poster_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    // Biến để mở Alert comment
    var openAlert = remember { mutableStateOf(false) }

    // Biến để lay danh sach binh luan cua phim
    val movie_id = detailState.movie?.id
    val commentList by remember(movie_id) {
        if (movie_id != null) {
            commentViewModel.getCommentsByMovieID(movie_id)
        } else {
            MutableLiveData(emptyList<Comment>())
        }
    }.observeAsState(initial = emptyList())

    // Lấy ra user_id để thêm phim vào danh sách yêu thích
    val userID by homeViewModel.userId.observeAsState(initial = -1)

    // Biến Icon yêu thích phim
    val likeIcon = R.drawable.like
    val likedIcon = R.drawable.liked
    var isLiked by rememberSaveable { mutableStateOf(false) }
    val iconsColor = MaterialTheme.colorScheme.onBackground
    val likedColor = if (isLiked) redd else iconsColor

    // Tạo lớp Viewmodel cho favourite bằng hilt
    val favouriteViewModel: FavouriteViewModel = hiltViewModel()

    // Check trạng thái yêu thích khi phim được tải
    LaunchedEffect(userID, movie_id) {
        if (userID != -1) {
            if (movie_id != null) {
                favouriteViewModel.checkisFavourite(movie_id, userID) { isFav ->
                    isLiked = isFav
                }
            }
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ){
        if (backDropImageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(
                        MaterialTheme.colorScheme.inverseSurface
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = detailState.movie?.title
                )
            }
        }

        if (backDropImageState is AsyncImagePainter.State.Success) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                painter = backDropImageState.painter,
                contentDescription = detailState.movie?.title,
                contentScale = ContentScale.Crop
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Box(modifier = Modifier
                .width(160.dp)
                .height(250.dp)
            ) {
                if (posterImageState is AsyncImagePainter.State.Error) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(70.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = detailState.movie?.title
                        )
                    }
                }

                if (posterImageState is AsyncImagePainter.State.Success) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        painter = posterImageState.painter,
                        contentDescription = detailState.movie?.title,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            detailState.movie?.let { movie ->
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = movie.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(dark_yellow, white_yellow)
                            )
                        ),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row (
                        modifier = Modifier
                            .padding(start = 16.dp)
                    ){
                        RatingBar(
                            starsModifier = Modifier.size(18.dp),
                            rating = movie.vote_average / 2
                        )

                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = movie.vote_average.toString().take(3),
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        color = Color.LightGray,
                        text = stringResource(R.string.language) + movie.original_language
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        color = Color.LightGray,
                        text = stringResource(R.string.release_date) + movie.release_date
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row (
                        modifier = Modifier.padding(start = 16.dp)
                    ){
                        Text(
                            color = Color.LightGray,
                            text = "${movie.vote_count} votes"
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        HandleIcon(
                            icon = if (isLiked) likedIcon else likeIcon,
                            contentDescription = "Like this movie",
                            color = likedColor
                        ) {
                            if (userID != -1) {
                                val newisLiked = !isLiked
                                if (movie_id != null) {
                                    favouriteViewModel.toggleFavourite(movie_id, userID, movie.title, newisLiked)
                                }
                                isLiked = newisLiked
                            }
                        }
                    }
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        color = Color.LightGray,
                        text = stringResource(R.string.genre) + " " +movie.genre
                    )

                    ElevatedButton(
                        onClick = { openAlert.value = true },
                        colors = ButtonDefaults.buttonColors(orange),
                        modifier = Modifier
                            .width(220.dp)
                            .padding(15.dp),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "Comment",
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Rounded.InsertComment,
                            contentDescription = "Status",
                            tint = Color.White
                        )
                    }
                    
                    if (openAlert.value) {
                        CustomDialogUIComment(openDialogBox = openAlert)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(18.dp))

        // Phần overview
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.overvieww),
            color = Color.LightGray,
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        detailState.movie?.let {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = it.overview,
                fontSize = 16.sp,
                color = Color.LightGray,
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "MOVIE TRAILER",
            fontSize = 35.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(orange, Purple80)
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        detailState.movie.let {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(start = 18.dp)
            ) {
                if (it != null) {
                    YouTubePlayer(
                        youtubeVideoId = it.videoTrailer,
                        lifecycleOwner = LocalLifecycleOwner.current)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "User comments section",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(dark_yellow, white_yellow)
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (commentList.isNullOrEmpty()) {
            Text(
                text = "No comment yet",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        } else {
            Column {
                commentList!!.forEach { item ->
                    CommentItem(item = item)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun CommentItem(item : Comment) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(light_back)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = SimpleDateFormat("HH:mm a, dd/MM", Locale.ENGLISH).format(item.createAt),
                fontSize = 12.sp,
                color = Color.Black
            )
            Text(
                text = item.account_name,
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(1.dp))
        Text(
            text = item.comment_text,
            fontSize = 15.sp,
            color = Color.Black,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis, // dấu ... khi số dòng vượt quá giới hạn
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun HandleIcon(
    @DrawableRes icon: Int,
    contentDescription: String,
    color: Color,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = icon),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(30.dp)
            .padding(5.dp)
            .clickable { onClick() }
            .offset(y = (-4).dp),
        colorFilter = ColorFilter.tint(color)
        )
}