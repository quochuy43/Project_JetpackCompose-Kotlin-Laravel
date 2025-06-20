package com.example.project_appmovie.movieList.data.remote

import com.example.project_appmovie.movieList.data.remote.respond.MovieListDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {
    @GET("movie/{category}")
    // Đây là danh sách lấy các bộ phim from web
    suspend fun getMovieList(
        @Path("category") category: String, // Giá trị sẽ được thêm vào đường dẫn
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieListDTO

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = "68d115553e55f9dafc5bc7804acaf6ac"
    }
}