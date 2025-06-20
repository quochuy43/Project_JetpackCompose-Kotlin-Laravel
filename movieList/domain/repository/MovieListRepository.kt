package com.example.project_appmovie.movieList.domain.repository

import com.example.project_appmovie.movieList.domain.model.Movie
import com.example.project_appmovie.movieList.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovie(id: Int): Flow<Resource<Movie>>
}