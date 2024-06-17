package com.example.project_appmovie.database.favourite_actors

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavouriteActorRepo @Inject constructor(
    private val favouriteActorDAO: AccountFavouriteActorDAO
) {
    suspend fun isFavouriteActor(actorID: Int, userID: Int): Boolean {
        return withContext(Dispatchers.IO) {
            favouriteActorDAO.isFavouriteActor(actorID, userID) != null
        }
    }

    suspend fun addFavouriteActor(actorID: Int, userID: Int, actorName: String) {
        withContext(Dispatchers.IO) {
            favouriteActorDAO.addActortoFavourites(
                AccountFavouriteActors(0, userID, actorID, actorName)
            )
        }
    }

    suspend fun removeFavourite(actorID: Int, userID: Int) {
        withContext(Dispatchers.IO) {
            favouriteActorDAO.removeActorFromFavourites(actorID, userID)
        }
    }

    fun getFavouriteActorsByUserID(userID: Int): LiveData<List<AccountFavouriteActors>> {
        return favouriteActorDAO.getFavouriteActorByUserID(userID)
    }

    suspend fun deleteItem(id: Int) {
        withContext(Dispatchers.IO) {
            favouriteActorDAO.deleteItem(id)
        }
    }
}