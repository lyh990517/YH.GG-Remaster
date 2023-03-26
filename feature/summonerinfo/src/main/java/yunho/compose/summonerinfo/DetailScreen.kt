package yunho.compose.summonerinfo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
    val itemList = remember{ mutableListOf<MatchState.Success>() }
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
                itemList.add(match)
                LazyColumn(){
                    items(itemList){
                        Text(text = "${it.matchData.info.gameId}")
                    }
                }
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