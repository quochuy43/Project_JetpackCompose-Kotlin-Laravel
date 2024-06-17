package com.example.project_appmovie.movieList.data.local.movie

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.project_appmovie.database.genres.Genres
import com.example.project_appmovie.movieList.data.mappers.toMovie
import com.example.project_appmovie.movieList.domain.model.Movie

@Dao
interface MovieDAO {
    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity

    @Query("SELECT * FROM MovieEntity WHERE category = :category")
    suspend fun getMovieListByCategory(category: String): List<MovieEntity>

    @Query("SELECT COUNT(*) FROM MovieEntity")
    suspend fun getTotalMovies(): Int

    @Query("SELECT COUNT(*) FROM MovieEntity WHERE category = :category")
    suspend fun getMoviesCountByCategory(category: String): Int

    // Tìm kiếm phim popular
    @Query("SELECT * FROM MovieEntity WHERE category = 'popular' AND title LIKE '%' || :name || '%'")
    suspend fun searchPopularMovies(name: String): List<MovieEntity>

    // Tìm kiếm phim upcoming
    @Query("SELECT * FROM MovieEntity WHERE category = 'upcoming' AND title LIKE '%' || :name || '%'")
    suspend fun searchUpcomingMovies(name: String): List<MovieEntity>

    // Xử lí với lọc thể loại trong popular
//    @Query("SELECT * FROM MovieEntity WHERE genre =:genre")
//    fun getPopularMoviesByGenre(genre: String): LiveData<List<MovieEntity>>
//
//    @Query("SELECT * FROM MovieEntity")
//    fun getAllPopularMovies(): LiveData<List<MovieEntity>>
//
    @Query("SELECT * FROM Genres")
    fun getAllGenres(): LiveData<List<Genres>>
//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenre(genre: Genres)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<Genres>)
}