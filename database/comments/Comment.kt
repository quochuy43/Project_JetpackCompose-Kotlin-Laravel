package com.example.project_appmovie.database.comments


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.project_appmovie.database.account.Account
import com.example.project_appmovie.movieList.data.local.movie.MovieEntity
import java.util.Date


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movie_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index(value = ["movie_id"])]
)
data class Comment (
    @PrimaryKey(autoGenerate = true)
    val comment_id: Int = 0,
    val movie_id: Int,
    val account_id: Int,
    val account_name: String,
    val comment_text: String,
    var createAt: Date
)