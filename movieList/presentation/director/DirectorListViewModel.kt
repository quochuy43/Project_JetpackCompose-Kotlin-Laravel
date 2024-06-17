package com.example.project_appmovie.movieList.presentation.director

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_appmovie.movieList.data.local.director.DirectorDAO
import com.example.project_appmovie.movieList.data.mappers.toDirector
import com.example.project_appmovie.movieList.domain.model.Director
import com.example.project_appmovie.movieList.domain.repository.DirectorListRepository
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
class DirectorListViewModel @Inject constructor(
    private val directorListRepository: DirectorListRepository,
    private val directorDAO: DirectorDAO
) : ViewModel() {
    private val _directorListState = MutableStateFlow(DirectorListState())
    val directorListState: StateFlow<DirectorListState> = _directorListState.asStateFlow()

    init {
        getDirectorList(false)
    }

    private fun getDirectorList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _directorListState.update {
                it.copy(isLoading = true)
            }

            directorListRepository.getDirectorList(forceFetchFromRemote).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _directorListState.update {
                            it.copy(isLoading = false, error = result.message)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { directorList ->
                            _directorListState.update {
                                it.copy(directorList = directorList, isLoading = false)
                            }
                        }
                    }
                    is Resource.Loading -> {
                        _directorListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }

    // Tim kiem dao dien
    private val _searchResults = MutableLiveData<List<Director>>()
    val searchResults: LiveData<List<Director>> get() = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchDirector(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val results = withContext(Dispatchers.IO) {
                directorDAO.searchDirector(query).map { it.toDirector() }
            }
            _searchResults.value = results
            _isLoading.value = false
        }
    }
}