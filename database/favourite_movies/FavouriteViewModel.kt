package com.example.project_appmovie.database.favourite_movies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    application: Application,
    private val repository: FavouriteRepository
) : AndroidViewModel(application) {

    fun checkisFavourite(movieId: Int, userId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isFav = repository.isFavourite(movieId, userId)
            callback(isFav)
        }
    }

    fun toggleFavourite(movieId: Int, userId: Int, movieName: String, isLiked: Boolean) {
        viewModelScope.launch {
            if (isLiked) {
                repository.addFavourite(movieId, userId, movieName)
            } else {
                repository.removeFavourite(movieId, userId)
            }
        }
    }

    fun getFavouriteMovies(userID: Int): LiveData<List<AccountFavouriteMovies>> {
        return repository.getFavouriteMoviesByUserID(userID)
    }

    fun deleteItem(id: Int) {
        viewModelScope.launch {
            repository.deleteItem(id)
        }
    }

}