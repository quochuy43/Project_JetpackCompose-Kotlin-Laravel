package com.example.project_appmovie.movieList.domain.repository

import com.example.project_appmovie.movieList.domain.model.Actor
import com.example.project_appmovie.movieList.util.Resource
import kotlinx.coroutines.flow.Flow

interface ActorListRepository {
    suspend fun getActorList(forceFetchFromRemote: Boolean): Flow<Resource<List<Actor>>>
    suspend fun getActor(id: Int): Flow<Resource<Actor>>
}