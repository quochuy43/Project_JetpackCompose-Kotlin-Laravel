package com.example.project_appmovie.movieList.data.remote

import com.example.project_appmovie.movieList.data.remote.respond.DirectorListDTO
import retrofit2.http.GET

interface DirectorAPI {
    @GET("api/director")
    suspend fun getDirectorList(): DirectorListDTO
    companion object {
        const val BASE_URL = "http://172.20.10.2:8000"
    }
}