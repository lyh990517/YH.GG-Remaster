package yunho.compose.yhgg_remaster

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import yunho.compose.screen.SearchScreen
import yunho.compose.summonerinfo.DetailScreen

@Composable
fun YH_GG_App(
    navigator: NavHostController = rememberNavController(),
) {
    NavHost(navController = navigator, startDestination = "search") {
        composable("search") {
            SearchScreen(navController = navigator)
        }
        composable(
            "info/{summoner}",
            arguments = listOf(navArgument("summoner") { type = NavType.StringType })
        ) {
            DetailScreen(
                navigator = navigator,
                summoner = it.arguments?.getString("summoner") ?: ""
            )
        }
        composable("info/{summoner}/{matchId}") {

        }
    }
}