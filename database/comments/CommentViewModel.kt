package com.example.project_appmovie.database.comments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.project_appmovie.DataApplication
import com.example.project_appmovie.MyDatabase
import com.example.project_appmovie.database.account.AccountDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val myDatabase: MyDatabase,
    application: Application,
    private val accountDAO: AccountDAO
): AndroidViewModel(application) {
    val commentDAO = myDatabase.getCommentDao()


    fun getCommentsByMovieID(movie_id: Int): LiveData<List<Comment>> {
        return commentDAO.getAllCommentbyMovieId(movie_id)
    }

    fun addComment(movie_id: Int, account_id: Int, account_name: String, comment_text: String) {
        viewModelScope.launch (Dispatchers.IO ) {
            val comment = Comment(movie_id = movie_id, account_id = account_id, account_name = account_name, comment_text = comment_text, createAt = Date.from(Instant.now()))
            commentDAO.addComment(comment)
        }
    }

}