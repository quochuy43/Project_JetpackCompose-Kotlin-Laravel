package com.example.project_appmovie.database.favourite_actors

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountFavouriteActorDAO {
    @Insert
    suspend fun addActortoFavourites(favouriteActors: AccountFavouriteActors)

    @Query("SELECT * FROM AccountFavouriteActors WHERE actor_id = :actorID AND user_id = :userID")
    suspend fun isFavouriteActor(actorID: Int, userID: Int): AccountFavouriteActors?

    @Query("DELETE FROM AccountFavouriteActors WHERE actor_id = :actorID AND user_id = :userID")
    suspend fun removeActorFromFavourites(actorID: Int, userID: Int)

    // Lay danh sach cac dien vien yeu thich
    @Query("SELECT * FROM AccountFavouriteActors WHERE user_id = :userID")
    fun getFavouriteActorByUserID(userID: Int): LiveData<List<AccountFavouriteActors>>

    // Xoa 1 dien vien tu danh sach yeu thich
    @Query("DELETE FROM AccountFavouriteActors WHERE favourite_actors_id = :itemID")
    suspend fun deleteItem(itemID: Int)

}