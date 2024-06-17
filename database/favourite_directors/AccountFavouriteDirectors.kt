package com.example.project_appmovie.database.favourite_directors

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.project_appmovie.database.account.Account
import com.example.project_appmovie.movieList.data.local.director.DirectorEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DirectorEntity::class,
            parentColumns = ["id"],
            childColumns = ["director_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AccountFavouriteDirectors (
    @PrimaryKey(autoGenerate = true) val favourite_directors_id: Int,
    val user_id: Int,
    val director_id: Int,
    val director_name: String
)