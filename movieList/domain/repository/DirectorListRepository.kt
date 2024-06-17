package com.example.project_appmovie.movieList.domain.repository

import com.example.project_appmovie.movieList.domain.model.Director
import com.example.project_appmovie.movieList.util.Resource
import kotlinx.coroutines.flow.Flow


interface DirectorListRepository {
    suspend fun getDirectorList(forceFetchFromRemote: Boolean): Flow<Resource<List<Director>>>
    suspend fun getDirector(id: Int): Flow<Resource<Director>>
}