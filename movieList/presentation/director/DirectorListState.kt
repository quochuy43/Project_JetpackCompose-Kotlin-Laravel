package com.example.project_appmovie.movieList.presentation.director

import com.example.project_appmovie.movieList.domain.model.Director

data class DirectorListState (
    val isLoading: Boolean = false,
    val directorList: List<Director> = emptyList(),
    val error: String? = null
)