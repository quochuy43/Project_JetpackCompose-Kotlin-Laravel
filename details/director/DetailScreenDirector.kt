package com.example.project_appmovie.details.director

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.project_appmovie.R
import com.example.project_appmovie.database.HomeViewModel
import com.example.project_appmovie.database.favourite_directors.FavouriteDirectorViewModel
import com.example.project_appmovie.ui.theme.Purple80
import com.example.project_appmovie.ui.theme.orange
import com.example.project_appmovie.ui.theme.redd
import com.example.project_appmovie.ui.theme.white_yellow

@Composable
fun DetailScreenDirector(
    backStackEntry: NavBackStackEntry,
    homeViewModel: HomeViewModel
) {
    // Chuyển link wiki
    val context = LocalContext.current
    val detailsViewModel = hiltViewModel<DetailsViewModelDirector>()
    val detailState = detailsViewModel.detailState.collectAsState().value

    val ImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(detailState.director?.profile_image_url)
            .size(Size.ORIGINAL)
            .build()
    ).state

    // Biến Icon yêu thích dao dien
    val likeIcon = R.drawable.like
    val likedIcon = R.drawable.liked
    var isLiked by rememberSaveable { mutableStateOf(false) }
    val iconsColor = MaterialTheme.colorScheme.onBackground
    val likedColor = if (isLiked) redd else iconsColor

    // Tạo lớp ViewModel bằng Hilt
    val favouriteDirectorViewModel: FavouriteDirectorViewModel = hiltViewModel()

    // Lấy ra id của diễn viên
    val directorID = detailState.director?.id
    val userID by homeViewModel.userId.observeAsState(initial = -1)

    LaunchedEffect(userID, directorID) {
        if (userID != -1) {
            if (directorID != null) {
                favouriteDirectorViewModel.checkisFavouriteDirector(directorID, userID) {isFav ->
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

        if (ImageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        MaterialTheme.colorScheme.inverseSurface
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = detailState.director?.name
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Box(modifier = Modifier
                .width(160.dp)
                .height(240.dp)
            ) {
                if (ImageState is AsyncImagePainter.State.Error) {
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
                            contentDescription = detailState.director?.name
                        )
                    }
                }

                if (ImageState is AsyncImagePainter.State.Success) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        painter = ImageState.painter,
                        contentDescription = detailState.director?.name,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            detailState.director?.let { director ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = director.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(orange, Purple80)
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        color = Color.White,
                        text = stringResource(R.string.age) + ": " + director.age
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        color = Color.White,
                        text = stringResource(R.string.nationality) + ": " + director.nationality
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ){
                        HandleIcon(
                            icon = if (isLiked) likedIcon else likeIcon,
                            contentDescription = "Like this actor",
                            color = likedColor
                        ) {
                            if (userID != -1) {
                                val newisLiked = !isLiked
                                if (directorID != null) {
                                    favouriteDirectorViewModel.toggleFavouriteDirector(
                                        directorID,
                                        userID,
                                        director.name,
                                        newisLiked
                                    )
                                }
                                isLiked = newisLiked
                            }
                        }

                        val link_wiki = director.link_wiki
                        val intent_wiki = Intent(Intent.ACTION_VIEW, Uri.parse(link_wiki))
                        IconButton(
                            onClick = {
                                context.startActivity(intent_wiki)
                            },
                            modifier = Modifier
                                .padding(start = 15.dp)
                                .background(redd)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "Info",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = "Famous Movie",
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(orange, Purple80)
                )
            ),
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        detailState.director?.let {
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = it.famous_movies,
                fontSize = 16.sp,
                color = orange,
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = "Information about Director",
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(orange, Purple80)
                )
            ),
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        detailState.director?.let {
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = it.information,
                fontSize = 16.sp,
                color = white_yellow,
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = "Outstanding Awards",
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(orange, Purple80)
                )
            ),
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        detailState.director?.let {
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = it.awards,
                fontSize = 16.sp,
                color = white_yellow,
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
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
            .size(65.dp)
            .padding(start = 18.dp)
            .clickable { onClick() },
        colorFilter = ColorFilter.tint(color)
    )
}