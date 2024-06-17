package com.example.project_appmovie.movieList.presentation

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.project_appmovie.R
import com.example.project_appmovie.database.account.AccountViewModel
import com.example.project_appmovie.database.HomeViewModel
import com.example.project_appmovie.database.genres.Genres
import com.example.project_appmovie.movieList.presentation.component.Actor
import com.example.project_appmovie.movieList.presentation.component.ActorCard
import com.example.project_appmovie.movieList.presentation.component.CountCard
import com.example.project_appmovie.movieList.presentation.component.EventMovie
import com.example.project_appmovie.movieList.presentation.component.MovieEventCard
import com.example.project_appmovie.movieList.presentation.movie.MovieListViewModel
import com.example.project_appmovie.ui.theme.PurpleGrey40
import com.example.project_appmovie.ui.theme.bluee
import com.example.project_appmovie.ui.theme.dark_back
import com.example.project_appmovie.ui.theme.dark_blue
import com.example.project_appmovie.ui.theme.dark_row
import com.example.project_appmovie.ui.theme.dark_yellow
import com.example.project_appmovie.ui.theme.light_back
import com.example.project_appmovie.ui.theme.orange
import com.example.project_appmovie.ui.theme.white_yellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun IntroductoryScreen(navController: NavHostController, homeViewModel: HomeViewModel = hiltViewModel()) {

    val images = mutableListOf(
        R.drawable.mainpage_slide7,
        R.drawable.mainpage_slide1,
        R.drawable.mainpage_slide2,
        R.drawable.mainpage_slide3,
        R.drawable.mainpage_slide4,
        R.drawable.mainpage_slide5,
        R.drawable.mainpage_slide6,
    )

    // Khai baso các biến để lấy thông tin người dùng từ HomeViewModel
    val userEmail by homeViewModel.userEmail.observeAsState("Unknown Email")
    val userPass by homeViewModel.userPass.observeAsState("Unknown Pass")
    val userName by homeViewModel.userName.observeAsState("Unknown Name")

    // Tạo thanh cuộn khi hết
    var scrollState = rememberScrollState()

    val pagerState = rememberPagerState(pageCount = {images.size})
    val matrix = remember {
        ColorMatrix()
    }

    // Hien thi avt
    val defaulAvatarBitmap = painterResource(id = R.drawable.def_avatar)
    val accountViewModel: AccountViewModel = hiltViewModel()


    LaunchedEffect(userEmail) {
        accountViewModel.loadUserAvatar(userEmail)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.scrollToPage(nextPage)
        }
    }

    val movieListViewModel: MovieListViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        val genress = listOf(
            Genres(1, "Action"),
            Genres(2, "Horror"),
            Genres(3, "Comedy"),
            Genres(4, "Cartoon"),
            Genres(5, "Romance"),
            Genres(6, "Science-Fiction"),
        )
        movieListViewModel.insertGenres(genress)
    }
    val genres by movieListViewModel.allGenres.observeAsState(emptyList())

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(scrollState)
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.5.dp)
                .background(dark_row, shape = RoundedCornerShape(10.dp)),
//                .border(width = 0.65.dp, color = white_yellow, shape = RoundedCornerShape(10.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .padding(5.dp)
                    .border(1.dp, white_yellow, CircleShape)
                    .clip(CircleShape), // Đặt hình tròn cho hình ảnh
                contentAlignment = Alignment.Center
            ) {
                val avatarBitmap = accountViewModel.userAvatar.collectAsState().value
                if (avatarBitmap != null) {
                    Image(
                        bitmap = avatarBitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = defaulAvatarBitmap,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(text = userName, fontSize = 20.sp, color = white_yellow, fontWeight = FontWeight.Bold)
                Text(text = userEmail, fontSize = 17.sp, color = white_yellow)
            }
            Spacer(modifier = Modifier.width(10.dp))
        }

        CountCardSection(
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "The Best Movies in 2024",
                fontSize = 16.sp,
                color = orange,
                modifier = Modifier.padding(start = 12.dp)
            )
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = null,
                    tint = orange
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.wrapContentSize()) {
                HorizontalPager(
                    state = pagerState,
                ) { index ->
                    val pageOffset = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
                    val imageSize by animateFloatAsState(
                        targetValue = if(pageOffset != 0.0f) 0.75f else 1f,
                        animationSpec = tween(durationMillis = 300)
                    )

                    LaunchedEffect(key1 = imageSize) {
                        if (pageOffset != 0.0f) {
                            matrix.setSaturation(0f)
                        } else {
                            matrix.setSaturation(1f)
                        }
                    }

                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .graphicsLayer {
                                scaleX = imageSize
                                scaleY = imageSize
                            },
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(images[index])
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Multiple Genres",
                fontSize = 16.sp,
                color = orange,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        GerneList(genres = genres)

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "The Greatest Actors in 2024",
                fontSize = 16.sp,
                color = orange,
                modifier = Modifier.padding(start = 12.dp)
            )
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.PersonAddAlt1,
                    contentDescription = null,
                    tint = orange
                )
            }
        }

        Spacer(modifier = Modifier.height(1.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            // Danh sách diễn viên
            var actorList = listOf(
                Actor("Tom Holland", R.drawable.movie_actor1, 40, "Australia"),
                Actor("Kate Winslet", R.drawable.movie_actor2, 48, "United Kingdom"),
                Actor("Benedict Cumberbatch", R.drawable.movie_actor3, 47, "United Kingdom"),
                Actor("Cillian Murphy", R.drawable.movie_actor4, 47, "Irish"),
                Actor("Jennifer Lawrence", R.drawable.movie_actor5, 33, "America"),
            )
            items(actorList){ actor ->
                ActorCard(
                    nameActor = actor.name,
                    imageActor = actor.image,
                    ageActor = actor.age,
                    countryActor = actor.country,
                    onclick = {}
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Featured Movie News in 2024",
                fontSize = 16.sp,
                color = orange,
                modifier = Modifier.padding(start = 12.dp)
            )
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = null,
                    tint = orange
                )
            }
        }

        Spacer(modifier = Modifier.height(1.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(23.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            // Danh sách diễn viên
            var eventList = listOf(
                EventMovie("Cannes Film Festival (May 14-25, 2024)", R.drawable.event1),
                EventMovie("Berlin International Film(February 15-25, 2024)", R.drawable.event2),
                EventMovie("Edinburgh International Film (June 12-23, 2024)", R.drawable.event3),
                EventMovie("Toronto International Film (September 5-15, 2024)", R.drawable.event4),
                EventMovie("Venice Film Festival (August 28 - September 7, 2024)", R.drawable.event5),
                EventMovie("BFI London Film Festival (October 2-13, 2024)", R.drawable.event6),
                EventMovie("Hong Kong International Film (March 28 - April 8, 2024)", R.drawable.event7)
            )
            items(eventList){ event ->
                MovieEventCard(
                    title = event.title,
                    image = event.image,
                    onclick = {}
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun CountCardSection(modifier: Modifier, viewModel: MovieListViewModel = hiltViewModel()) {

    val totalMovies by viewModel.totalMovies.collectAsState()
    val popularMovies by viewModel.popularMovies.collectAsState()
    val upcomingMovies by viewModel.upcomingMovies.collectAsState()

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
            .background(
                light_back,
                shape = RoundedCornerShape(20.dp)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Total movies",
            count = totalMovies
        )

        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Popular Movies",
            count = popularMovies
        )

        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Upcoming Movies",
            count = upcomingMovies
        )
    }
}

// Hien thi danh sach cac the loai phim
@Composable
fun GerneList(genres: List<Genres>) {
    LazyRow (
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
    ){
        items(genres) { genre ->
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(dark_back)
            ) {
                Text(
                    text = genre.name,
                    fontWeight = FontWeight.Bold,
                    color = white_yellow
                )
            }
        }
    }
}

