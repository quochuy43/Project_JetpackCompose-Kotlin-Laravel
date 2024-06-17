package com.example.project_appmovie.movieList.data.repository

import android.util.Log
import com.example.project_appmovie.MyDatabase
import com.example.project_appmovie.movieList.data.mappers.toActor
import com.example.project_appmovie.movieList.data.mappers.toActorEntity
import com.example.project_appmovie.movieList.data.mappers.toMovieEntity
import com.example.project_appmovie.movieList.data.remote.ActorAPI
import com.example.project_appmovie.movieList.domain.model.Actor
import com.example.project_appmovie.movieList.domain.repository.ActorListRepository
import com.example.project_appmovie.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class ActorListRepositoryImpl @Inject constructor(
    private val actorApi: ActorAPI,
    private val movieDatabase: MyDatabase
) : ActorListRepository {
    override suspend fun getActorList(
        forceFetchFromRemote: Boolean
    ): Flow<Resource<List<Actor>>> {
        return flow {
            Log.d("ActorListRepository", "getActorList called")
            emit(Resource.Loading(true))

            val localActorList = movieDatabase.actorDAO.getAllActors()
            Log.d("ActorListRepository", "Local Actor List: $localActorList")

            val shouldLoadLocalActor = localActorList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalActor) {
                emit(Resource.Success(
                    data = localActorList.map { actorEntity ->
                        actorEntity.toActor()
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }

            val actorListFromApi = try {
                actorApi.getActorList()
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading actors"))
                return@flow
            }
            catch (e: retrofit2.HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading actors"))
                return@flow
            }
            catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading actors"))
                return@flow
            }

            val actorEntities = actorListFromApi.results.let {
                it.map { actorDTO ->
                    actorDTO.toActorEntity()
                }
            }

            movieDatabase.actorDAO.upsertActorList(actorEntities)

            emit(Resource.Success(
                actorEntities.map { it.toActor() }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getActor(id: Int): Flow<Resource<Actor>> {
        return flow {
            emit(Resource.Loading(true))
            val actorEntity = movieDatabase.actorDAO.getActorById(id)
            if (actorEntity != null) {
                emit(
                    Resource.Success(actorEntity.toActor())
                )

                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("Error no such actor"))
            emit(Resource.Loading(false))
        }
    }
}
