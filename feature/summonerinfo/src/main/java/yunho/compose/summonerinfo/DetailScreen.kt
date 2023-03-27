package yunho.compose.summonerinfo

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import yunho.compose.domain.model.LeagueEntryDTO
import yunho.compose.domain.model.MatchDTO
import yunho.compose.domain.model.SummonerDTO
import yunho.compose.yhgg_remaster.presentaion.state.MatchState
import yunho.compose.yhgg_remaster.presentaion.state.SummonerState
import yunho.compose.yhgg_remaster.presentaion.viewmodel.MatchViewModel
import yunho.compose.yhgg_remaster.presentaion.viewmodel.SummonerViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    summoner: String,
    navigator: NavController,
    summonerViewModel: SummonerViewModel,
    matchViewModel: MatchViewModel
) {
    LaunchedEffect(Unit) {
        summonerViewModel.getSummonerInfo(summoner)

    }
    val summonerState by summonerViewModel.summonerState.collectAsState()
    val matchState by matchViewModel.matchState.collectAsState()
    val matchList = rememberSaveable { mutableListOf<MatchState.Success>() }
    val summonerLeague = remember { mutableStateOf(dummy) }
    val currentSummoner = remember { mutableStateOf(dummySummonerDTO) }

    when (summonerState) {
        is SummonerState.Loading -> {

        }
        is SummonerState.Success -> {
            val summonerInfo = summonerState as SummonerState.Success
            currentSummoner.value = summonerInfo.infoData
            LaunchedEffect(Unit) {
                matchViewModel.getMatchIds(summonerInfo.infoData.puuid)
                summonerViewModel.getSummonerLeague(summonerInfo.infoData.id)
            }
        }
        is SummonerState.LoadLeagueEntry -> {
            val leagueEntryDTO = summonerState as SummonerState.LoadLeagueEntry
            summonerLeague.value =
                leagueEntryDTO.data.ifEmpty { listOf() }
        }
        is SummonerState.Error -> {

        }
    }
    when (matchState) {
        is MatchState.Loading -> {

        }
        is MatchState.LoadIds -> {
            val ids = matchState as MatchState.LoadIds
            ids.ids.forEach {
                LaunchedEffect(Unit) {
                    matchViewModel.getMatchInfo(it)
                }
            }
        }
        is MatchState.Success -> {
            val match = matchState as MatchState.Success
            matchList.add(match)
        }
        is MatchState.Error -> {

        }
    }
    Scaffold() {
        Column {
            SummonerView(
                Modifier.weight(1f),
                leagueEntryDTO = summonerLeague.value,
                summonerDTO = currentSummoner.value
            )
            MatchView(
                Modifier.weight(3f),
                itemList = matchList
            )
        }
    }
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
fun SummonerView(
    modifier: Modifier = Modifier,
    leagueEntryDTO: List<LeagueEntryDTO>,
    summonerDTO: SummonerDTO
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.teemo),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth
        )
        Column(modifier, verticalArrangement = Arrangement.Bottom) {
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = ""
                )
                Text(text = "test")
            }
            Row() {
                Button(onClick = {}) {

                }
                Button(onClick = {}) {

                }
            }
        }
    }
}

@Composable
private fun MatchView(modifier: Modifier = Modifier, itemList: MutableList<MatchState.Success>) {
    LazyColumn(modifier = modifier) {
        items(itemList) {
            MatchItem(it.matchData)
        }
    }
}

@Composable
fun TopImageView(modifier: Modifier = Modifier, summonerDTO: SummonerDTO) {
}

@Composable
fun RankView(modifier: Modifier = Modifier) {

}

@Composable
fun MatchItem(matchData: MatchDTO) {
    Text(text = "${matchData.info.participants}")
}

@Preview
@Composable
fun MatchItemPreview() {

}

@Preview
@Composable
fun TopImageViewPreview() {
    TopImageView(summonerDTO = dummySummonerDTO)
}

@Preview
@Composable
fun SummonerViewPreview() {
    SummonerView(leagueEntryDTO = dummy, summonerDTO = dummySummonerDTO)
}

@Preview
@Composable
fun MatchViewPreview() {
    MatchView(itemList = mutableListOf())
}

val dummy =
    listOf(
        LeagueEntryDTO(
            leagueId = "dummyLeagueId",
            summonerId = "dummySummonerId",
            summonerName = "Dummy Summoner Name",
            queueType = "dummyQueueType",
            tier = "dummyTier",
            rank = "dummyRank",
            leaguePoints = 0,
            wins = 0,
            losses = 0,
            hotStreak = false,
            veteran = false,
            freshBlood = false,
            inactive = false,
            miniSeries = null
        )
    )

val dummySummonerDTO = SummonerDTO(
    accountId = "dummyAccountId",
    profileIconId = 123,
    revisionDate = 1616389029000,
    name = "Dummy Summoner",
    id = "dummySummonerId",
    puuid = "dummyPuuid",
    summonerLevel = 100
)