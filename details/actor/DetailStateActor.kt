package com.example.project_appmovie.details.actor

import com.example.project_appmovie.movieList.domain.model.Actor

data class DetailStateActor(
    val isLoading: Boolean = false,
    val actor: Actor? = null
)