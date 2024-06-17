package com.example.project_appmovie.database.favourite_movies

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavouriteRepository @Inject constructor(
    private val favouriteMovieDAO: AccountFavouriteMovieDAO
) {
    suspend fun isFavourite(movieID: Int, userID: Int): Boolean {
        return withContext(Dispatchers.IO) {
            favouriteMovieDAO.isFavourite(movieID, userID) != null
        }
    }

    suspend fun addFavourite(movieId: Int, userId: Int, movieName: String) {
        withContext(Dispatchers.IO) {
            favouriteMovieDAO.addMovieToFavourites(
                AccountFavouriteMovies(0, userId, movieId, movieName)
            )
        }
    }

    suspend fun removeFavourite(movieId: Int, userId: Int) {
        withContext(Dispatchers.IO) {
            favouriteMovieDAO.removeMovieFromFavourites(movieId, userId)
        }
    }

    fun getFavouriteMoviesByUserID(userID: Int): LiveData<List<AccountFavouriteMovies>> {
        return favouriteMovieDAO.getFavouriteMoviesByUserID(userID)
    }

    suspend fun deleteItem(id: Int) {
        withContext(Dispatchers.IO) {
            favouriteMovieDAO.deleteItem(id)
        }
    }
}