package com.example.project_appmovie.movieList.presentation

import android.app.Activity
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.project_appmovie.database.genres.Genres
import com.example.project_appmovie.movieList.domain.model.Movie
import com.example.project_appmovie.movieList.presentation.component.MovieItem
import com.example.project_appmovie.movieList.presentation.movie.MovieListState
import com.example.project_appmovie.movieList.presentation.movie.MovieListUIEvent
import com.example.project_appmovie.movieList.presentation.movie.MovieListViewModel
import com.example.project_appmovie.movieList.util.Category
import com.example.project_appmovie.ui.theme.white_yellow
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularMovieScreen(
    movieListState: MovieListState,
    navController: NavHostController,
    movieListViewModel: MovieListViewModel,
    onEvent: (MovieListUIEvent) -> Unit // Không có giá trị trả về, tương tự như void
) {
    var textSearch by remember { mutableStateOf("") }
    val myColor = Color(0xFF6AF8A5)
    val searchResults by movieListViewModel.searchResults.observeAsState(emptyList())
    val isLoading by movieListViewModel.isLoading.observeAsState(false)

    // Lọc phim theo thể loại
    val genres by movieListViewModel.allGenres.observeAsState(emptyList())
//    val originalMoviesListState by remember { mutableStateOf<List<Movie>>(emptyList()) }
//    val filteredMovies by movieListViewModel.moviesByGenre.observeAsState(emptyList())


    // Tim kiem bang giong noi
    val context = LocalContext.current
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult())
    {
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            spokenText?.let {
                textSearch = it
                movieListViewModel.searchPopularMovies(it)
            }
        } else {
            Toast.makeText(context, "Didn't catch that. Speak again", Toast.LENGTH_SHORT).show()
        }
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TextField(
                value = textSearch,
                onValueChange = {
                    textSearch = it
                    movieListViewModel.searchPopularMovies(it) },
                modifier = Modifier.padding(top = 16.dp, bottom = 6.dp),
                label = { Text(text = "Search for Popular Movies") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH)
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something")
                        }
                        speechRecognizerLauncher.launch(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Mic,
                            contentDescription = null,
                            tint = white_yellow
                        )
                    }
                },
                textStyle = TextStyle(fontSize = 20.sp), // Đặt kích thước chữ ở đây
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedLabelColor = myColor,
                    focusedLabelColor = myColor,
                    cursorColor = myColor,
                    focusedLeadingIconColor = myColor,
                    unfocusedLeadingIconColor = myColor,
                    containerColor = myColor.copy(alpha = .2f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = CutCornerShape(15.dp)
            )
        }


        // Hien thi danh sach the loai
//        GerneList(genres = genres)

        val displayList = if (textSearch.isEmpty()) {
            movieListState.popularMovieList
        } else {
            searchResults
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            displayList.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No movies found")
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
                ) {
                    items(displayList.size) { index ->
                        val movie = displayList[index]
                        if (movie != null) {
                            MovieItem(
                                movie = movie,
                                navHostController = navController
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        if (index >= displayList.size - 1 && !movieListState.isLoading && textSearch.isEmpty()) {
                            onEvent(MovieListUIEvent.Paginate(Category.POPULAR))
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//fun GerneList(genres: List<Genres>, onGenreSelected: (String) -> Unit) {
//    LazyRow {
//        items(genres) { genre ->
//            Button(onClick = { onGenreSelected(genre.name) }) {
//                Text(text = genre.name)
//            }
//        }
//    }
//}

//@Composable
//fun GerneList(genres: List<Genres>) {
//    LazyRow {
//        items(genres) { genre ->
//            Button(onClick = { /*TODO*/ }) {
//                Text(text = genre.name)
//            }
//        }
//    }
//}