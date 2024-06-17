package com.example.project_appmovie.database.favourite_actors

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.project_appmovie.database.account.Account
import com.example.project_appmovie.movieList.data.local.actor.ActorEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ActorEntity::class,
            parentColumns = ["id"],
            childColumns = ["actor_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AccountFavouriteActors (
    @PrimaryKey(autoGenerate = true) val favourite_actors_id: Int,
    val user_id: Int,
    val actor_id: Int,
    val actor_name: String
)