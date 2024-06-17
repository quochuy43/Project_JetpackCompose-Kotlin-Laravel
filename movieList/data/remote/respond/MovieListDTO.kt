package com.example.project_appmovie.movieList.data.remote.respond

// Data transfer object
data class MovieListDTO(
    val page: Int,
    val results: List<MovieDTO>,
    val total_pages: Int,
    val total_results: Int
)