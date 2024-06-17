package com.example.project_appmovie.database.favourite_directors

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavouriteDirectorRepo @Inject constructor(
    private val favouriteDirectorDAO: AccountFavouriteDirectorDAO
) {
    suspend fun isFavouriteDirector(directorID: Int, userID: Int): Boolean {
        return withContext(Dispatchers.IO) {
            favouriteDirectorDAO.isFavouriteDirector(directorID, userID) != null
        }
    }

    suspend fun addFavouriteDirector(directorID: Int, userID: Int, directorName: String) {
        withContext(Dispatchers.IO) {
            favouriteDirectorDAO.addDirectortoFavourites(
                AccountFavouriteDirectors(0, userID, directorID, directorName)
            )
        }
    }

    suspend fun removeFavourite(directorID: Int, userID: Int) {
        withContext(Dispatchers.IO) {
            favouriteDirectorDAO.removeDirectorFromFavourites(directorID, userID)
        }
    }

    fun getFavouriteDirectorsByUserID(userID: Int): LiveData<List<AccountFavouriteDirectors>> {
        return favouriteDirectorDAO.getFavouriteDirectorByUserID(userID)
    }

    suspend fun deleteItem(id: Int) {
        withContext(Dispatchers.IO) {
            favouriteDirectorDAO.deleteItem(id)
        }
    }
}