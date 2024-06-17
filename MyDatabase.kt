package com.example.project_appmovie

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.project_appmovie.database.account.Account
import com.example.project_appmovie.database.account.AccountDAO
import com.example.project_appmovie.database.comments.Comment
import com.example.project_appmovie.database.comments.CommentDAO
import com.example.project_appmovie.database.comments.Converters
import com.example.project_appmovie.database.favourite_actors.AccountFavouriteActorDAO
import com.example.project_appmovie.database.favourite_actors.AccountFavouriteActors
import com.example.project_appmovie.database.favourite_directors.AccountFavouriteDirectorDAO
import com.example.project_appmovie.database.favourite_directors.AccountFavouriteDirectors
import com.example.project_appmovie.database.favourite_movies.AccountFavouriteMovieDAO
import com.example.project_appmovie.database.favourite_movies.AccountFavouriteMovies
import com.example.project_appmovie.database.genres.Genres
import com.example.project_appmovie.movieList.data.local.actor.ActorDAO
import com.example.project_appmovie.movieList.data.local.actor.ActorEntity
import com.example.project_appmovie.movieList.data.local.director.DirectorDAO
import com.example.project_appmovie.movieList.data.local.director.DirectorEntity
import com.example.project_appmovie.movieList.data.local.movie.MovieDAO
import com.example.project_appmovie.movieList.data.local.movie.MovieEntity

@Database(entities = [
    MovieEntity::class,
    Account::class,
    ActorEntity::class,
    DirectorEntity::class,
    Comment::class,
    AccountFavouriteMovies::class,
    AccountFavouriteActors::class,
    AccountFavouriteDirectors::class,
    Genres::class], version = 11)
@TypeConverters(Converters::class)
abstract class MyDatabase: RoomDatabase() {

    // Account
    abstract fun getAccountDao(): AccountDAO

    // Movie
    abstract val movieDAO: MovieDAO

    // Actor
    abstract val actorDAO: ActorDAO

    // Director
    abstract val directorDAO: DirectorDAO

    // Comment
    abstract fun getCommentDao(): CommentDAO

    // Favourite
    abstract fun accountFavouriteMoviesDao(): AccountFavouriteMovieDAO
    abstract fun accountFavouriteActorsDao(): AccountFavouriteActorDAO
    abstract fun accountFavouriteDirectorsDao(): AccountFavouriteDirectorDAO

    companion object {
        const val DATABASE_NAME = "My_Database"
    }
}