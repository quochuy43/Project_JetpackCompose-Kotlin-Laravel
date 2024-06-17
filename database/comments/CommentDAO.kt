package com.example.project_appmovie.database.comments

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CommentDAO {

    @Query("SELECT * FROM Comment WHERE movie_id = :movie_id")
    fun getAllCommentbyMovieId(movie_id: Int): LiveData<List<Comment>>

    @Insert
    fun addComment(comment: Comment)

}