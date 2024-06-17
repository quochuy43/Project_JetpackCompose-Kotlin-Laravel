package com.example.project_appmovie.movieList.data.remote.respond

import androidx.room.PrimaryKey

data class ActorDTO(
    val id:Int,
    val name: String,
    val age: Int,
    val nationality: String,
    val information: String,
    val profile_image_url: String,
    val famous_movies: String,
    val awards: String,
    val link_ig: String
)