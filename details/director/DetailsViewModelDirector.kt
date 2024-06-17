package com.example.project_appmovie.details.director

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_appmovie.movieList.domain.repository.DirectorListRepository
import com.example.project_appmovie.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModelDirector @Inject constructor(
    private val directorListRepository: DirectorListRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){
    private val directorID = savedStateHandle.get<Int>("directorID")

    private var _detailsState = MutableStateFlow(DetailStateDirector())
    val detailState = _detailsState.asStateFlow()

    init {
        getDirector(directorID ?: -1)
    }

    private fun getDirector(id: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoading = true)
            }

            directorListRepository.getDirector(id).collectLatest { result ->
                when(result) {
                    is Resource.Error -> {
                        _detailsState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _detailsState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { director ->
                            _detailsState.update {
                                it.copy(director = director)
                            }
                        }
                    }
                }
            }
        }
    }
}