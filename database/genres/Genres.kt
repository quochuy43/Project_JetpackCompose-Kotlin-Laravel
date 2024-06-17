package com.example.project_appmovie.database.genres

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Genres (
    @PrimaryKey val genreId: Int,
    val name: String
)