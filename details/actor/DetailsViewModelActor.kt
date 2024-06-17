package com.example.project_appmovie.details.actor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_appmovie.movieList.domain.repository.ActorListRepository
import com.example.project_appmovie.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModelActor @Inject constructor(
    private val actorListRepository: ActorListRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val actorID = savedStateHandle.get<Int>("actorID")

    private var _detailsState = MutableStateFlow(DetailStateActor())
    val detailState = _detailsState.asStateFlow()

    init {
        getActor(actorID ?: -1)
    }

    private fun getActor(id: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoading = true)
            }

            actorListRepository.getActor(id).collectLatest { result ->
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
                        result.data?.let { actor ->
                            _detailsState.update {
                                it.copy(actor = actor)
                            }
                        }
                    }
                }
            }
        }
    }
}