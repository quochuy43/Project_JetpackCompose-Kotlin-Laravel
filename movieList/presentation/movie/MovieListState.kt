package com.example.project_appmovie.movieList.presentation.movie

import com.example.project_appmovie.movieList.domain.model.Movie

data class MovieListState(
    val isLoading: Boolean = false,

    val popularMovieListPage: Int = 1,
    val upcomingMovieListPage: Int = 1,

    val isMainScreen: Boolean = true,
    val isPopularScreen: Boolean = true,
    val isUpcomingScreen: Boolean = true,

    val popularMovieList: List<Movie> = emptyList(),
    val upcomingMovieList: List<Movie> = emptyList()
)