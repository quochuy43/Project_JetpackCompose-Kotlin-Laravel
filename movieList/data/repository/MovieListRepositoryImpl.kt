package com.example.project_appmovie.movieList.data.repository

import android.net.http.HttpException
import android.util.Log
import com.example.project_appmovie.MyDatabase
import com.example.project_appmovie.movieList.data.mappers.toMovie
import com.example.project_appmovie.movieList.data.mappers.toMovieEntity
import com.example.project_appmovie.movieList.data.remote.MovieAPI
import com.example.project_appmovie.movieList.domain.model.Movie
import com.example.project_appmovie.movieList.domain.repository.MovieListRepository
import com.example.project_appmovie.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieAPI,
    private val movieDatabase: MyDatabase
) : MovieListRepository {
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            Log.d("Huy", "getMovieList called")
            emit(Resource.Loading(true))

            val localMovieList = movieDatabase.movieDAO.getMovieListByCategory(category)
            Log.d("Huy", "Local Movie List: $localMovieList")

            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalMovie) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }

            val movieListFromApi = try {
                movieApi.getMovieList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }
            catch (e: retrofit2.HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }
            catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }

            val movieEntities = movieListFromApi.results.let {
                it.map { movieDTO ->
                    movieDTO.toMovieEntity(category)
                }
            }

            movieDatabase.movieDAO.upsertMovieList(movieEntities)

            emit(Resource.Success(
                movieEntities.map { it.toMovie(category) }
            ))
            emit(Resource.Loading(false))

        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))
            val movieEntity = movieDatabase.movieDAO.getMovieById(id)
            if (movieEntity != null) {
                emit(
                    Resource.Success(movieEntity.toMovie(movieEntity.category))
                )

                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("Error no such movie"))
            emit(Resource.Loading(false))
        }
    }
}