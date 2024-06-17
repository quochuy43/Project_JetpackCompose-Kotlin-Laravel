package com.example.project_appmovie.movieList.presentation.actor

import com.example.project_appmovie.movieList.domain.model.Actor

data class ActorListState(
    val isLoading: Boolean = false,
    val actorList: List<Actor> = emptyList(),
    val error: String? = null
)