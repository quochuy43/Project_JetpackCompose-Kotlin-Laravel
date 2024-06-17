package com.example.project_appmovie.movieList.presentation.movie

// Sử dụng sealed để đảm bảo tính nhất quán cho ứng dụng
sealed interface MovieListUIEvent {
    data class Paginate(val category: String): MovieListUIEvent
    object Navigate: MovieListUIEvent
}