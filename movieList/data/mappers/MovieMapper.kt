package com.example.project_appmovie.movieList.data.mappers

import com.example.project_appmovie.movieList.data.local.movie.MovieEntity
import com.example.project_appmovie.movieList.data.remote.respond.MovieDTO
import com.example.project_appmovie.movieList.domain.model.Movie

fun MovieDTO.toMovieEntity(
    category: String
): MovieEntity {
    return MovieEntity(
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        overview = overview ?: "",
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count?: 0,
        video = video ?: false,
        id = id ?: -1,
        adult = adult ?: false,
        original_title = original_title ?: "",
        category = category,
        genre = genre ?: "",
        videoTrailer = videoTrailer ?: ""
    )
}

fun MovieEntity.toMovie(
    category: String
): Movie {
    return Movie(
        backdrop_path = backdrop_path,
        original_language = original_language,
        overview = overview,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        vote_average = vote_average,
        popularity = popularity,
        vote_count = vote_count,
        video = video,
        id = id,
        adult = adult,
        original_title = original_title,
        category = category,
        genre = genre,
        videoTrailer = videoTrailer
    )
}