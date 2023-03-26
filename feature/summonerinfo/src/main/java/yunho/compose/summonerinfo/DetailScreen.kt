package yunho.compose.summonerinfo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import yunho.compose.yhgg_remaster.presentaion.state.MatchState
import yunho.compose.yhgg_remaster.presentaion.state.SummonerState
import yunho.compose.yhgg_remaster.presentaion.viewmodel.MatchViewModel
import yunho.compose.yhgg_remaster.presentaion.viewmodel.SummonerViewModel

@Composable
fun DetailScreen(
    summoner: String,
    navigator: NavController,
    summonerViewModel: SummonerViewModel,
    matchViewModel: MatchViewModel
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            summonerViewModel.getSummonerInfo(summoner)
        }
    }
    val summonerState = summonerViewModel.summonerState.collectAsState()
    val matchState = matchViewModel.matchState.collectAsState()
    Column {
        Text(text = "${summonerState.value}")
        when (summonerState.value) {
            is SummonerState.Success -> {
                val summoner = summonerState.value as SummonerState.Success
                LaunchedEffect(Unit) {
                    matchViewModel.getMatchIds(summoner.infoData.puuid)
                }
            }
            else -> {}
        }
        when (matchState.value) {
            is MatchState.Success -> {
                val match = matchState.value as MatchState.Success
                Text(text = match.toString())
            }
            is MatchState.LoadIds -> {
                val ids = matchState.value as MatchState.LoadIds
                ids.ids.forEach {
                    LaunchedEffect(Unit){
                        matchViewModel.getMatchInfo(it)
                    }
                }
            }
            else -> {}
        }
    }
    BackHandler {
        navigator.popBackStack()
    }
}

@Preview
@Composable
fun DetailScreenPreview() {

}