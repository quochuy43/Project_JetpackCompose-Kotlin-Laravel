package com.example.project_appmovie.movieList.presentation.actor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_appmovie.movieList.data.local.actor.ActorDAO
import com.example.project_appmovie.movieList.data.mappers.toActor
import com.example.project_appmovie.movieList.domain.model.Actor
import com.example.project_appmovie.movieList.domain.repository.ActorListRepository
import com.example.project_appmovie.movieList.util.Category
import com.example.project_appmovie.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ActorListViewModel @Inject constructor(
    private val actorListRepository: ActorListRepository,
    private val actorDAO: ActorDAO
) : ViewModel() {

    private val _actorListState = MutableStateFlow(ActorListState())
    val actorListState: StateFlow<ActorListState> = _actorListState.asStateFlow()

    init {
        Log.d("ActorListViewModel", "init called")
        getActorList(false)
    }

    private fun getActorList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            Log.d("ActorListViewModel", "getActorList called with forceFetchFromRemote: $forceFetchFromRemote")
            _actorListState.update {
                it.copy(isLoading = true)
            }

            actorListRepository.getActorList(forceFetchFromRemote).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        Log.e("ActorListViewModel", "Error loading actors: ${result.message}")
                        _actorListState.update {
                            it.copy(isLoading = false, error = result.message)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { actorList ->
                            Log.d("ActorListViewModel", "Success: $actorList")
                            _actorListState.update {
                                it.copy(actorList = actorList, isLoading = false)
                            }
                        }
                    }
                    is Resource.Loading -> {
                        Log.d("ActorListViewModel", "Loading: ${result.isLoading}")
                        _actorListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }
    // Tim kiem phim
    private val _searchResults = MutableLiveData<List<Actor>>()
    val searchResults: LiveData<List<Actor>> get() = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchActor(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val results = withContext(Dispatchers.IO) {
                actorDAO.searchActor(query).map { it.toActor() }
            }
            _searchResults.value = results
            _isLoading.value = false
        }
    }
}

