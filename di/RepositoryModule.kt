package com.example.project_appmovie.di

import com.example.project_appmovie.movieList.data.repository.ActorListRepositoryImpl
import com.example.project_appmovie.movieList.data.repository.DirectorListRepositoryImpl
import com.example.project_appmovie.movieList.data.repository.MovieListRepositoryImpl
import com.example.project_appmovie.movieList.domain.repository.ActorListRepository
import com.example.project_appmovie.movieList.domain.repository.DirectorListRepository
import com.example.project_appmovie.movieList.domain.repository.MovieListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMovieListRepository(
        movieListRepositoryImpl: MovieListRepositoryImpl
    ): MovieListRepository

    @Binds
    @Singleton
    abstract fun bindActorListRepository(
        actorListRepositoryImpl: ActorListRepositoryImpl
    ): ActorListRepository

    @Binds
    @Singleton
    abstract fun bindDirectorListRepository(
        directorListRepositoryImpl: DirectorListRepositoryImpl
    ): DirectorListRepository
}