package com.example.project_appmovie.UI_View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonPin
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project_appmovie.R
import com.example.project_appmovie.database.HomeViewModel
import com.example.project_appmovie.movieList.presentation.IntroductoryScreen
import com.example.project_appmovie.movieList.presentation.PersonListScreen
import com.example.project_appmovie.movieList.presentation.movie.MovieListUIEvent
import com.example.project_appmovie.movieList.presentation.movie.MovieListViewModel
import com.example.project_appmovie.movieList.presentation.PopularMovieScreen
import com.example.project_appmovie.movieList.presentation.ProfileScreen
import com.example.project_appmovie.movieList.presentation.UpcomingMovieScreen
import com.example.project_appmovie.movieList.presentation.actor.ActorListViewModel
import com.example.project_appmovie.movieList.presentation.director.DirectorListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiScreen(navController: NavHostController) {

    val movieListViewModel: MovieListViewModel = hiltViewModel()
    val actorListViewModel: ActorListViewModel = hiltViewModel()
    val directorListViewModel: DirectorListViewModel = hiltViewModel()
    val movieListState by movieListViewModel.movieListState.collectAsState()
    val actorListState by actorListViewModel.actorListState.collectAsState()
    val directorListState by directorListViewModel.directorListState.collectAsState()
    val bottomNavController = rememberNavController()


    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                bottomNavController = bottomNavController,
                onEvent = movieListViewModel::onEvent
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = "introduce"
            ) {
                composable(route = "introduce") {
                    val homeViewModel: HomeViewModel = hiltViewModel()
                    IntroductoryScreen(navController = navController, homeViewModel = homeViewModel)
                }
                composable(route = "popularMovie") {
                    PopularMovieScreen(
                        movieListState = movieListState,
                        navController = navController,
                        movieListViewModel = movieListViewModel,
                        onEvent = movieListViewModel::onEvent
                    )
                }
                composable(route = "upcomingMovie") {
                    UpcomingMovieScreen(
                        movieListState = movieListState,
                        navController = navController,
                        movieListViewModel = movieListViewModel,
                        onEvent = movieListViewModel::onEvent
                    )
                }
                composable(route = "listactors") {
                    PersonListScreen(
                        actorListState = actorListState,
                        directorListState = directorListState,
                        actorListViewModel = actorListViewModel,
                        directorListViewModel = directorListViewModel,
                        navController = navController
                    )
                }
                composable(route = "profile") {
                    val homeViewModel: HomeViewModel = hiltViewModel()
                    ProfileScreen(navController = navController, homeViewModel = homeViewModel)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    bottomNavController: NavHostController,
    onEvent: (MovieListUIEvent) -> Unit
) {
    val items = listOf(
        BottomItem(
            title = stringResource(R.string.main_page),
            icon = Icons.Rounded.Home
        ),
        BottomItem(
            title = stringResource(R.string.popular),
            icon = Icons.Rounded.Movie
        ),
        BottomItem(
            title = stringResource(R.string.upcoming),
            icon = Icons.Rounded.Upcoming
        ),
        BottomItem(
            title = stringResource(R.string.artist),
            icon = Icons.Rounded.PersonPin
        ),
        BottomItem(
            title = stringResource(R.string.profile),
            icon = Icons.Rounded.Person
        )
    )

    val selected = rememberSaveable { mutableStateOf(0) }

    NavigationBar {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(
                    selected = selected.value == index,
                    onClick = {
                        selected.value = index
                        when (index) {
                            0 -> {
                                bottomNavController.navigate("introduce") {
                                    popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            1 -> {
                                bottomNavController.navigate("popularMovie") {
                                    popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            2 -> {
                                bottomNavController.navigate("upcomingMovie") {
                                    popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            3 -> {
                                bottomNavController.navigate("listactors") {
                                    popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            4 -> {
                                bottomNavController.navigate("profile") {
                                    popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                        onEvent(MovieListUIEvent.Navigate)
                    },
                    icon = {
                        Icon(
                            imageVector = bottomItem.icon,
                            contentDescription = bottomItem.title,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    label = {
                        Text(
                            text = bottomItem.title,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }
    }
}

data class BottomItem(val title: String, val icon: ImageVector)


