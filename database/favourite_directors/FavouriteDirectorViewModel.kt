package com.example.project_appmovie.database.favourite_directors

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteDirectorViewModel @Inject constructor(
    application: Application,
    private val repository: FavouriteDirectorRepo
) : AndroidViewModel(application){

    fun checkisFavouriteDirector(directorID: Int, userID: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isFav = repository.isFavouriteDirector(directorID, userID)
            callback(isFav)
        }
    }

    fun toggleFavouriteDirector(directorID: Int, userID: Int, directorName: String, isLiked: Boolean) {
        viewModelScope.launch {
            if (isLiked) {
                repository.addFavouriteDirector(directorID, userID, directorName)
            } else {
                repository.removeFavourite(directorID, userID)
            }
        }
    }

    fun getFavouriteDirector(userID: Int): LiveData<List<AccountFavouriteDirectors>> {
        return repository.getFavouriteDirectorsByUserID(userID)
    }

    fun deleleItem(id: Int) {
        viewModelScope.launch {
            repository.deleteItem(id)
        }
    }
}