package com.example.project_appmovie.database.favourite_directors

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountFavouriteDirectorDAO {
    @Insert
    suspend fun addDirectortoFavourites(favouriteDirectors: AccountFavouriteDirectors)

    @Query("SELECT * FROM AccountFavouriteDirectors WHERE director_id = :directorID AND user_id = :userID")
    suspend fun isFavouriteDirector(directorID: Int, userID: Int): AccountFavouriteDirectors?

    @Query("DELETE FROM AccountFavouriteDirectors WHERE director_id = :directorID AND user_id = :userID")
    suspend fun removeDirectorFromFavourites(directorID: Int, userID: Int)

    // Lay danh sach cac dien vien yeu thich
    @Query("SELECT * FROM AccountFavouriteDirectors WHERE user_id = :userID")
    fun getFavouriteDirectorByUserID(userID: Int): LiveData<List<AccountFavouriteDirectors>>

    // Xoa 1 dien vien tu danh sach yeu thich
    @Query("DELETE FROM AccountFavouriteDirectors WHERE favourite_directors_id = :itemID")
    suspend fun deleteItem(itemID: Int)

}