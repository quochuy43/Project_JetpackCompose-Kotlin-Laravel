package com.example.project_appmovie.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.example.project_appmovie.database.HomeViewModel
import com.example.project_appmovie.database.favourite_actors.AccountFavouriteActors
import com.example.project_appmovie.database.favourite_actors.FavouriteActorViewModel
import com.example.project_appmovie.database.favourite_directors.AccountFavouriteDirectors
import com.example.project_appmovie.database.favourite_directors.FavouriteDirectorViewModel
import com.example.project_appmovie.database.favourite_movies.AccountFavouriteMovies
import com.example.project_appmovie.database.favourite_movies.FavouriteViewModel
import com.example.project_appmovie.ui.theme.PurpleGrey40
import com.example.project_appmovie.ui.theme.dark_blue
import com.example.project_appmovie.ui.theme.dark_yellow
import com.example.project_appmovie.ui.theme.white_yellow

@Composable
fun CustomDialogFavourite(openDialogBox: MutableState<Boolean>, navController: NavHostController) {
    Dialog(onDismissRequest = { openDialogBox.value = false }) {
        CustomUI(openDialogBox, homeViewModel = hiltViewModel(), favouriteViewModel = hiltViewModel(), favouriteActorViewModel = hiltViewModel(), favouriteDirectorViewModel = hiltViewModel(), navController = navController)
    }
}

@Composable
private fun CustomUI(
    openDialog: MutableState<Boolean>,
    homeViewModel: HomeViewModel,
    favouriteViewModel: FavouriteViewModel,
    favouriteActorViewModel: FavouriteActorViewModel,
    favouriteDirectorViewModel: FavouriteDirectorViewModel,
    navController: NavHostController) {

    // Thanh cuộn
    val scrollState = rememberScrollState()

    val userID by homeViewModel.userId.observeAsState(initial = -1)

    // Danh sach phim yeu thich
    val favouriteList by remember(userID) {
        if (userID != null) {
            favouriteViewModel.getFavouriteMovies(userID)
        } else {
            MutableLiveData(emptyList<AccountFavouriteMovies>())
        }
    }.observeAsState(initial = emptyList())

    // Danh sach dien vien yeu thich
    val favouriteListActor by remember(userID) {
        if (userID != null) {
            favouriteActorViewModel.getFavouriteActor(userID)
        } else {
            MutableLiveData(emptyList<AccountFavouriteActors>())
        }
    }.observeAsState(initial = emptyList())

    // Danh sach dien vien yeu thich
    val favouriteListDirectors by remember(userID) {
        if (userID != null) {
            favouriteDirectorViewModel.getFavouriteDirector(userID)
        } else {
            MutableLiveData(emptyList<AccountFavouriteDirectors>())
        }
    }.observeAsState(initial = emptyList())


    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(white_yellow)
                .verticalScroll(scrollState)
        ) {
            Column(Modifier.padding(6.dp)) {
                Text(
                    text = "MY PLAYLIST",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = dark_yellow,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (favouriteList.isNullOrEmpty() && favouriteListActor.isNullOrEmpty() && favouriteListDirectors.isNullOrEmpty()) {
                Text(
                    text = "You don't have a favorite list yet",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Column {
                    favouriteList!!.forEach { item ->
                        FavouriteItem(
                            item = item,
                            onDelete = { favouriteViewModel.deleteItem(item.favourite_movies_id)},
                            navController = navController
                        )
                    }
                }
                Column {
                    favouriteListActor!!.forEach { item ->
                        FavouriteActorItem(
                            item = item,
                            onDelete = { favouriteActorViewModel.deleleItem(item.favourite_actors_id)},
                            navController = navController
                        )
                    }
                }
                Column {
                    favouriteListDirectors!!.forEach { item ->
                        FavouriteDirectorItem(
                            item = item,
                            onDelete = { favouriteDirectorViewModel.deleleItem(item.favourite_directors_id)},
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavouriteItem(item: AccountFavouriteMovies, onDelete: () -> Unit, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(dark_yellow)
            .padding(10.dp)
            .clickable {navController.navigate("details"+"/${item.movie_id}")},
        verticalAlignment = Alignment.CenterVertically

    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "MOVIE - ${item.movie_name}",
                fontSize = 18.sp,
                color = white_yellow,
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }
}

@Composable
fun FavouriteActorItem(item: AccountFavouriteActors, onDelete: () -> Unit, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(dark_blue)
            .padding(10.dp)
            .clickable { navController.navigate("actor_details" + "/${item.actor_id}")},
        verticalAlignment = Alignment.CenterVertically

    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "ACTOR - ${item.actor_name}",
                fontSize = 18.sp,
                color = white_yellow,
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }
}

@Composable
fun FavouriteDirectorItem(item: AccountFavouriteDirectors, onDelete: () -> Unit, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(PurpleGrey40)
            .padding(10.dp)
            .clickable { navController.navigate("director_details" + "/${item.director_id}")},
        verticalAlignment = Alignment.CenterVertically

    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "DIRECTOR - ${item.director_name}",
                fontSize = 18.sp,
                color = white_yellow,
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }
}