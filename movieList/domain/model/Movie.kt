package com.example.project_appmovie.movieList.domain.model
data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val genre: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val id: Int,
    val category: String,
    val videoTrailer: String,
)