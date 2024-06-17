package com.example.project_appmovie.movieList.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.project_appmovie.movieList.domain.model.Director
import com.example.project_appmovie.movieList.presentation.actor.ActorListState
import com.example.project_appmovie.movieList.domain.model.Actor
import com.example.project_appmovie.movieList.presentation.actor.ActorListViewModel
import com.example.project_appmovie.movieList.presentation.component.ActorItem
import com.example.project_appmovie.movieList.presentation.component.DirectorItem
import com.example.project_appmovie.movieList.presentation.director.DirectorListState
import com.example.project_appmovie.movieList.presentation.director.DirectorListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonListScreen(
    actorListState: ActorListState,
    directorListState: DirectorListState,
    actorListViewModel: ActorListViewModel,
    directorListViewModel: DirectorListViewModel,
    navController: NavHostController
) {
    var textSearch by remember { mutableStateOf("") }
    val myColor = Color(0xFF6AF8A5)

    val searchActorResults by actorListViewModel.searchResults.observeAsState(emptyList())
    val isLoadingActor by actorListViewModel.isLoading.observeAsState(false)

    val searchDirectorResults by directorListViewModel.searchResults.observeAsState(emptyList())
    val isLoadingDirector by directorListViewModel.isLoading.observeAsState(false)

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TextField(
                value = textSearch,
                onValueChange = {
                    textSearch = it
                    actorListViewModel.searchActor(it)
                    directorListViewModel.searchDirector(it) },
                modifier = Modifier.padding(top = 16.dp, bottom = 6.dp),
                label = { Text(text = "Search Actor, Director") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                textStyle = TextStyle(fontSize = 20.sp), // Đặt kích thước chữ ở đây
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedLabelColor = myColor,
                    focusedLabelColor = myColor,
                    cursorColor = myColor,
                    focusedLeadingIconColor = myColor,
                    unfocusedLeadingIconColor = myColor,
                    containerColor = myColor.copy(alpha = .2f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = CutCornerShape(15.dp)
            )
        }

        val displayListActor = if (textSearch.isEmpty()) {
            actorListState.actorList
        } else {
            searchActorResults
        }

        val displayListDirector = if (textSearch.isEmpty()) {
            directorListState.directorList
        } else {
            searchDirectorResults
        }

        when {
            isLoadingActor -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            isLoadingDirector -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            displayListActor.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No actors found")
                }
            }

            displayListDirector.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No director found")
                }
            }

            else -> {
                val combinedList = mutableListOf<Any>().apply {
                    addAll(displayListActor)
                    addAll(displayListDirector)
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
                ) {
                    items(combinedList.size) {index ->
                        val item = combinedList[index]
                        when (item) {
                            is Actor -> {
                                ActorItem(
                                    actor = item,
                                    navHostController = navController
                                )
                            }
                            is Director -> {
                                DirectorItem(
                                    director = item,
                                    navHostController = navController
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

//        if (actorListState.actorList.isEmpty() && directorListState.directorList.isEmpty()) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        } else {
//            val combinedList = mutableListOf<Any>().apply {
//                addAll(actorListState.actorList)
//                addAll(directorListState.directorList)
//            }
//
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(2),
//                modifier = Modifier.fillMaxSize(),
//                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
//            ) {
//                items(combinedList.size) {index ->
//                    val item = combinedList[index]
//                    when (item) {
//                        is Actor -> {
//                            ActorItem(
//                                actor = item,
//                                navHostController = navController
//                            )
//                        }
//                        is Director -> {
//                            DirectorItem(
//                                director = item,
//                                navHostController = navController
//                            )
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//            }
//        }
    }
}
