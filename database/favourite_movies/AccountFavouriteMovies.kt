package com.example.project_appmovie.database.favourite_movies

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.project_appmovie.database.account.Account
import com.example.project_appmovie.movieList.data.local.movie.MovieEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movie_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AccountFavouriteMovies(
    @PrimaryKey(autoGenerate = true) val favourite_movies_id: Int,
    val user_id: Int,
    val movie_id: Int,
    val movie_name: String
)
