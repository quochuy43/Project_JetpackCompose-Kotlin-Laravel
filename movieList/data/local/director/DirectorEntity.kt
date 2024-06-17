package com.example.project_appmovie.movieList.data.local.director

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DirectorEntity(
    val age: Int,
    val awards: String,
    val famous_movies: String,
    @PrimaryKey val id: Int,
    val information: String,
    val name: String,
    val nationality: String,
    val profile_image_url: String,
    val link_wiki: String
)