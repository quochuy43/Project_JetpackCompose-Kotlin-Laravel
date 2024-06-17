package com.example.project_appmovie.movieList.data.repository

import android.util.Log
import com.example.project_appmovie.MyDatabase
import com.example.project_appmovie.movieList.data.mappers.toDirector
import com.example.project_appmovie.movieList.data.mappers.toDirectorEntity
import com.example.project_appmovie.movieList.data.remote.DirectorAPI
import com.example.project_appmovie.movieList.domain.model.Director
import com.example.project_appmovie.movieList.domain.repository.DirectorListRepository
import com.example.project_appmovie.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DirectorListRepositoryImpl @Inject constructor(
    private val directorApi: DirectorAPI,
    private val movieDatabase: MyDatabase
) : DirectorListRepository{
    override suspend fun getDirectorList(forceFetchFromRemote: Boolean): Flow<Resource<List<Director>>> {
        return flow {
            Log.d("DirectorListRepository", "getDirectorList called")
            emit(Resource.Loading(true))

            val localDirectorList = movieDatabase.directorDAO.getAllDirectors()
            Log.d("DirectorListRepository", "Local Director List: $localDirectorList")

            val shouldLoadLocalDirector = localDirectorList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalDirector) {
                emit(Resource.Success(
                    data = localDirectorList.map { directorEntity ->
                        directorEntity.toDirector()
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }

            val directorListFromApi = try {
                directorApi.getDirectorList()
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading directors"))
                return@flow
            }
            catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading directors"))
                return@flow
            }
            catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading directors"))
                return@flow
            }

            val directorEntities = directorListFromApi.results.let {
                it.map { directorDTO ->
                    directorDTO.toDirectorEntity()
                }
            }

            movieDatabase.directorDAO.upsertDirectorList(directorEntities)

            emit(Resource.Success(
                directorEntities.map { it.toDirector() }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getDirector(id: Int): Flow<Resource<Director>> {
        return flow {
            emit(Resource.Loading(true))
            val directorEntity = movieDatabase.directorDAO.getDirectorById(id)
            if (directorEntity != null) {
                emit(
                    Resource.Success(directorEntity.toDirector())
                )

                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("Error no such director"))
            emit(Resource.Loading(false))
        }
    }
}