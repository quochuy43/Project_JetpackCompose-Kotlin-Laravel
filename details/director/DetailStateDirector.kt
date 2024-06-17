package com.example.project_appmovie.details.director

import com.example.project_appmovie.movieList.domain.model.Director

data class DetailStateDirector (
    val isLoading: Boolean = false,
    val director: Director? = null
)