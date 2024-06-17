package com.example.project_appmovie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.project_appmovie.UI_View.StartedScreen
import com.example.project_appmovie.login_signup_view.LoginScreen
import com.example.project_appmovie.login_signup_view.SignUpScreen
import com.example.project_appmovie.database.account.AccountViewModel
import com.example.project_appmovie.UI_View.MultiScreen
import com.example.project_appmovie.database.HomeViewModel
import com.example.project_appmovie.database.comments.CommentViewModel
import com.example.project_appmovie.details.actor.DetailScreenActor
import com.example.project_appmovie.details.director.DetailScreenDirector
import com.example.project_appmovie.details.movie.DetailScreenMovie


@Composable
fun Navigationn() {
    // Dialog để xem trailer
    val navController = rememberNavController()
//    val currentRoute = curentRoute(navController = navController)

    // Lấy accountViewModel, commentViewModel thông qua hiltViewModel()
    val accountViewModel: AccountViewModel = hiltViewModel()
    val commentViewModel: CommentViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "login") {

        composable(route = "login") {
            LoginScreen(navController, accountViewModel)
        }

        composable(route = "signup") {
            SignUpScreen(navController, accountViewModel)
        }

        composable(route = "started") {
            StartedScreen(navController)
        }

        composable(route = "main") {
            MultiScreen(navController)
        }

        composable(route = "details" + "/{movieID}",
            arguments = listOf(
                navArgument("movieID") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            DetailScreenMovie(backStackEntry, commentViewModel, homeViewModel)
        }

        composable(route = "actor_details" + "/{actorID}",
            arguments = listOf(
                navArgument("actorID") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            DetailScreenActor(backStackEntry, homeViewModel)
        }

        composable(route = "director_details" + "/{directorID}",
            arguments = listOf(
                navArgument("directorID") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            DetailScreenDirector(backStackEntry, homeViewModel)
        }
    }
}



