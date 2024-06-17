package com.example.project_appmovie.movieList.presentation.movie

import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.project_appmovie.database.genres.Genres
import com.example.project_appmovie.movieList.data.local.movie.MovieDAO
import com.example.project_appmovie.movieList.data.local.movie.MovieEntity
import com.example.project_appmovie.movieList.data.mappers.toMovie
import com.example.project_appmovie.movieList.domain.model.Movie
import com.example.project_appmovie.movieList.domain.repository.MovieListRepository
import com.example.project_appmovie.movieList.presentation.movie.MovieListUIEvent
import com.example.project_appmovie.movieList.util.Category
import com.example.project_appmovie.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    private val movieDAO: MovieDAO
): ViewModel() {

    private val _totalMovies = MutableStateFlow(0)
    val totalMovies: StateFlow<Int> = _totalMovies

    private val _popularMovies = MutableStateFlow(0)
    val popularMovies: StateFlow<Int> = _popularMovies

    private val _upcomingMovies = MutableStateFlow(0)
    val upcomingMovies: StateFlow<Int> = _upcomingMovies

    // Biến ni để lưu trữ trạng thái danh sách phim, MovieListState() là trạng thái ban đầu
    private var _movieListState = MutableStateFlow(MovieListState())
    // Không thể thay đổi, chỉ đọc để cung cấp truy cập công khai đến màn hình
    val movieListState = _movieListState.asStateFlow()

    // Khởi tạo ngay sau khi view model được tạo ra, gọi để tải danh sách phim
    init {
        getPopularMovieList(false)
        getUpcomingMovieList(false)
        fetchMovieCounts()
    }

    fun onEvent(event: MovieListUIEvent) {
        when (event) {
            // Trường hợp ni thì viewmodel được cập nhật để chuyển đổi màn hình chính phổ biến với cái phim tiếp theo
            MovieListUIEvent.Navigate -> {
                _movieListState.update { currentState ->
                    currentState.copy(
                        isMainScreen = when {
                            currentState.isMainScreen -> false // Nếu đang ở màn hình chính, chuyển sang màn hình khác
                            else -> true // Nếu không, chuyển về màn hình chính
                        },
                        isPopularScreen = when {
                            currentState.isMainScreen -> true // Nếu chuyển đến màn hình chính, hiển thị danh sách phim phổ biến
                            else -> currentState.isPopularScreen // Giữ nguyên trạng thái của màn hình phổ biến nếu đang ở màn hình khác
                        },
                        isUpcomingScreen = when {
                            currentState.isMainScreen -> true // Nếu chuyển đến màn hình chính, hiển thị danh sách phim sắp tới
                            else -> currentState.isUpcomingScreen // Giữ nguyên trạng thái của màn hình sắp tới nếu đang ở màn hình khác
                        }
                    )
                }
            }



            // Tham số true đại diện cho việc buộc repository lấy dữ liệu từ xa thay vì lấy từ cơ sở dữ liệu cục bộ
            is MovieListUIEvent.Paginate -> {
                // Gọi để lấy danh sách phim
                if (event.category == Category.POPULAR) {
                    getPopularMovieList(true)
                } else if (event.category == Category.UPCOMING) {
                    getUpcomingMovieList(true)
                }
            }
        }
    }

    // Tham số truyền vô buộc repository có cho phép lấy dữ liệu từ xa hay không
    private fun getPopularMovieList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            // Trước khi đi lấy dữ liệu thì cập nhật trạng thái loading
            _movieListState.update {
                it.copy(isLoading = true)
            }
            // Gọi hàm để lấy danh sách phim phổ biến
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.POPULAR,
                movieListState.value.popularMovieListPage
                // collectLatest là lắng nghe dữ liệu trả về từ luồng dữ liệu
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { popularList ->
                            _movieListState.update {
                                it.copy(
                                    popularMovieList = movieListState.value.popularMovieList + popularList.shuffled(),
                                    popularMovieListPage = movieListState.value.popularMovieListPage + 1
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }

    private fun getUpcomingMovieList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.UPCOMING,
                movieListState.value.upcomingMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { upcomingList ->
                            _movieListState.update {
                                it.copy(
                                    upcomingMovieList = movieListState.value.upcomingMovieList + upcomingList.shuffled(),
                                    upcomingMovieListPage = movieListState.value.upcomingMovieListPage + 1
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }
    private fun fetchMovieCounts() {
        viewModelScope.launch {
            _totalMovies.value = movieDAO.getTotalMovies()
            _popularMovies.value = movieDAO.getMoviesCountByCategory("popular")
            _upcomingMovies.value = movieDAO.getMoviesCountByCategory("upcoming")
        }
    }

    // Tìm kiếm phim
    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> get() = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchPopularMovies(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val results = withContext(Dispatchers.IO) {
                movieDAO.searchPopularMovies(query).map { it.toMovie("popular") }
            }
            _searchResults.value = results
            _isLoading.value = false
        }
    }

    fun searchUpcomingMovies(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val results = withContext(Dispatchers.IO) {
                movieDAO.searchUpcomingMovies(query).map { it.toMovie("upcoming") }
            }
            _searchResults.value = results
            _isLoading.value = false
        }
    }

    // Xu li chuc nang loc phim
    val allGenres: LiveData<List<Genres>> = movieDAO.getAllGenres()
//
//    private val _selectedGenreName = MutableLiveData<String>()
//    private val _movies = movieDAO.getAllPopularMovies()
//
//    val moviesByGenre = MediatorLiveData<List<Movie>>().apply {
//        // Thêm nguồn dữ liệu _movies
//        addSource(_movies) { movies ->
//            value = if (_selectedGenreName.value.isNullOrEmpty()) {
//                // Nếu không có thể loại được chọn, trả về tất cả phim
//                movies.map { it.toMovie("popular") }
//            } else {
//                // Nếu có thể loại được chọn, lọc danh sách phim theo thể loại và chuyển đổi thành đối tượng Movie
//                val filteredMovies = movies.filter { movie ->
//                    movie.genre.equals(_selectedGenreName.value)
//                }.map { it.toMovie("popular") }
//                filteredMovies
//            }
//        }
//
//        // Thêm nguồn dữ liệu _selectedGenreName
//        addSource(_selectedGenreName) { genreName ->
//            value = if (genreName.isNullOrEmpty()) {
//                // Nếu không có thể loại được chọn, trả về tất cả phim
//                _movies.value?.map { it.toMovie("popular") }
//            } else {
//                // Nếu có thể loại được chọn, lọc danh sách phim theo thể loại và chuyển đổi thành đối tượng Movie
//                _movies.value?.filter { movie ->
//                    movie.genre.equals(genreName)
//                }?.map { it.toMovie("popular") }
//            }
//        }
//    }
//
//    fun setGenre(genreName: String) {
//        _selectedGenreName.value = genreName
//    }
//
    fun insertGenre(genre: Genres) {
        viewModelScope.launch {
            movieDAO.insertGenre(genre)
        }
    }

    fun insertGenres(genres: List<Genres>) {
        viewModelScope.launch {
            movieDAO.insertGenres(genres)
        }
    }
}
