package com.example.project_appmovie.movieList.data.local.director

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface DirectorDAO {
    @Upsert
    suspend fun upsertDirectorList(directorList: List<DirectorEntity>)

    @Query("SELECT * FROM DirectorEntity WHERE id = :id")
    suspend fun getDirectorById(id: Int): DirectorEntity?

    @Query("SELECT * FROM DirectorEntity")
    suspend fun getAllDirectors(): List<DirectorEntity>

    // Tim kiem dao dien
    @Query("SELECT * FROM DirectorEntity WHERE name LIKE '%' || :name || '%'")
    suspend fun searchDirector(name: String): List<DirectorEntity>
}