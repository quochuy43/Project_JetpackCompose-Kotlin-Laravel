package com.example.project_appmovie.details.movie

import com.example.project_appmovie.movieList.domain.model.Movie

data class DetailStateMovie(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)