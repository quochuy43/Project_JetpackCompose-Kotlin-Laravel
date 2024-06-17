package com.example.project_appmovie.movieList.data.local.actor

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ActorDAO {
    @Upsert
    suspend fun upsertActorList(actorList: List<ActorEntity>)

    @Query("SELECT * FROM ActorEntity WHERE id = :id")
    suspend fun getActorById(id: Int): ActorEntity?

    @Query("SELECT * FROM ActorEntity")
    suspend fun getAllActors(): List<ActorEntity>

    // Tìm kiếm diễn viên
    @Query("SELECT * FROM ActorEntity WHERE name LIKE '%' || :name || '%'")
    suspend fun searchActor(name: String): List<ActorEntity>
}