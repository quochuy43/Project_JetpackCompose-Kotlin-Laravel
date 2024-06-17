package com.example.project_appmovie.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.project_appmovie.MyDatabase
import com.example.project_appmovie.database.account.AccountDAO
import com.example.project_appmovie.database.comments.CommentDAO
import com.example.project_appmovie.database.favourite_actors.AccountFavouriteActorDAO
import com.example.project_appmovie.database.favourite_directors.AccountFavouriteDirectorDAO
import com.example.project_appmovie.database.favourite_movies.AccountFavouriteMovieDAO
import com.example.project_appmovie.dialog.Mailer
import com.example.project_appmovie.movieList.data.local.actor.ActorDAO
import com.example.project_appmovie.movieList.data.local.director.DirectorDAO
import com.example.project_appmovie.movieList.data.local.movie.MovieDAO
import com.example.project_appmovie.movieList.data.remote.ActorAPI
import com.example.project_appmovie.movieList.data.remote.DirectorAPI
import com.example.project_appmovie.movieList.data.remote.MovieAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun providesMovieApi(): MovieAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MovieAPI.BASE_URL)
            .client(client)
            .build()
            .create(MovieAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesActorApi(): ActorAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ActorAPI.BASE_URL)
            .client(client)
            .build()
            .create(ActorAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesDirectorApi(): DirectorAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(DirectorAPI.BASE_URL)
            .client(client)
            .build()
            .create(DirectorAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesMyDatabase(application: Application): MyDatabase {
        return Room.databaseBuilder(
            application,
            MyDatabase::class.java,
            MyDatabase.DATABASE_NAME
        )   .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDAO(database: MyDatabase): MovieDAO {
        return database.movieDAO
    }

    @Provides
    @Singleton
    fun provideActorDAO(database: MyDatabase): ActorDAO {
        return database.actorDAO
    }

    @Provides
    @Singleton
    fun provideDirectorDAO(database: MyDatabase): DirectorDAO {
        return database.directorDAO
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    fun provideAccountDao(database: MyDatabase): AccountDAO {
        return database.getAccountDao()
    }

    @Provides
    fun provideCommentDao(database: MyDatabase): CommentDAO {
        return database.getCommentDao()
    }

    @Provides
    @Singleton
    fun provideAccountFavouriteMovieDAO(database: MyDatabase): AccountFavouriteMovieDAO {
        return database.accountFavouriteMoviesDao()
    }

    @Provides
    @Singleton
    fun provideAccountFavouriteActorDAO(database: MyDatabase): AccountFavouriteActorDAO {
        return database.accountFavouriteActorsDao()
    }

    @Provides
    @Singleton
    fun provideAccountFavouriteDirectorDAO(database: MyDatabase): AccountFavouriteDirectorDAO {
        return database.accountFavouriteDirectorsDao()
    }

    @Provides
    @Singleton
    fun provideMailer(): Mailer {
        return Mailer
    }
}