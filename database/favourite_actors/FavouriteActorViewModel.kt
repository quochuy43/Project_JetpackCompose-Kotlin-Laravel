package com.example.project_appmovie.database.favourite_actors

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteActorViewModel @Inject constructor(
    application: Application,
    private val repository: FavouriteActorRepo
) : AndroidViewModel(application){

    fun checkisFavouriteActor(actorID: Int, userID: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isFav = repository.isFavouriteActor(actorID, userID)
            callback(isFav)
        }
    }

    fun toggleFavouriteActor(actorID: Int, userID: Int, actorName: String, isLiked: Boolean) {
        viewModelScope.launch {
            if (isLiked) {
                repository.addFavouriteActor(actorID, userID, actorName)
            } else {
                repository.removeFavourite(actorID, userID)
            }
        }
    }

    fun getFavouriteActor(userID: Int): LiveData<List<AccountFavouriteActors>> {
        return repository.getFavouriteActorsByUserID(userID)
    }

    fun deleleItem(id: Int) {
        viewModelScope.launch {
            repository.deleteItem(id)
        }
    }
}