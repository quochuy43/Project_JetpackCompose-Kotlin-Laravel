package com.example.project_appmovie.database.favourite_movies

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountFavouriteMovieDAO {
    @Insert
    suspend fun addMovieToFavourites(favouriteMovies: AccountFavouriteMovies)

    @Query("SELECT * FROM AccountFavouriteMovies WHERE movie_id = :movieId AND user_id = :userId")
    suspend fun isFavourite(movieId: Int, userId: Int): AccountFavouriteMovies?

    @Query("DELETE FROM AccountFavouriteMovies WHERE movie_id = :movieId AND user_id = :userId")
    suspend fun removeMovieFromFavourites(movieId: Int, userId: Int)

    // Lấy ra danh sách phim yêu thích thuộc về 1 tài khoản
    @Query("SELECT * FROM AccountFavouriteMovies WHERE user_id = :userId")
    fun getFavouriteMoviesByUserID(userId: Int): LiveData<List<AccountFavouriteMovies>>

    // Xoa phim yeu thich tu danh sach yeu thich qua icon
    @Query("Delete FROM AccountFavouriteMovies WHERE favourite_movies_id = :itemID")
    suspend fun deleteItem(itemID: Int)
}