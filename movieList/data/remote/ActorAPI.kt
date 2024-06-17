package com.example.project_appmovie.movieList.data.remote

import com.example.project_appmovie.movieList.data.remote.respond.ActorListDTO
import retrofit2.http.GET

interface ActorAPI {
    @GET("api/actor")
    suspend fun getActorList(): ActorListDTO
    companion object {
        const val BASE_URL = "http://172.20.10.2:8000"
    }
}