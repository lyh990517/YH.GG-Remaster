package yunho.compose.summonerinfo

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
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
    val scrollState = rememberScrollState(0)
    Scaffold {
        Column {
            TopScrollContent(
                scrollState,
                leagueEntryDTO = summonerLeague.value,
                summonerDTO = currentSummoner.value
            )
            SummonerView(
                Modifier,
                leagueEntry = summonerLeague.value,
                scrollState = scrollState
            )
            MatchView(
                Modifier.weight(1f),
                itemList = matchList,
                scrollState = scrollState
            )
        }
    }
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
fun TopScrollContent(
    scrollState: ScrollState,
    leagueEntryDTO: List<LeagueEntryDTO>,
    summonerDTO: SummonerDTO
) {
    val dynamicHeight = (250f - scrollState.value).coerceIn(130f, 250f)
    val modifier = Modifier
        .heightIn(min = animateDpAsState(targetValue = dynamicHeight.dp).value)
        .fillMaxWidth()
    BoxWithConstraints(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "backGround",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .heightIn(max = animateDpAsState(targetValue = dynamicHeight.dp).value)
                .fillMaxSize(),
        )
        Column(
            modifier.heightIn(
                max = animateDpAsState(
                    targetValue = dynamicHeight.dp
                ).value
            ).fillMaxSize(), verticalArrangement = Arrangement.Bottom
        ) {
            Row(modifier = modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.profile_icon),
                    contentDescription = "profile Image",
                    Modifier
                        .padding(15.dp)
                        .width(100.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "${summonerDTO.name}",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 15.dp),
                    fontStyle = FontStyle.Normal,
                    color = Color.White
                )
            }
        }
        Text(
            text = "${summonerDTO.summonerLevel}",
            modifier = Modifier
                .padding(start = 50.dp, top = 100.dp),
            color = Color.White,
            fontSize = 20.sp
        )
    }
}

@Composable
fun SummonerView(
    modifier: Modifier,
    leagueEntry: List<LeagueEntryDTO>,
    scrollState: ScrollState
) {
    val dynamicHeight = (150f - scrollState.value).coerceIn(0f, 150f)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .heightIn(
                min = animateDpAsState(
                    targetValue = dynamicHeight.dp
                ).value
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.background2),
            contentDescription = "",
            modifier
                .heightIn(
                    max = animateDpAsState(
                        targetValue = dynamicHeight.dp
                    ).value
                )
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        LazyColumn(
            modifier.heightIn(
                max = animateDpAsState(
                    targetValue = dynamicHeight.dp
                ).value
            )
        ) {
            items(leagueEntry) {
                RankItem(leagueEntry = it)
            }
        }
    }
}

@Composable
fun RankItem(leagueEntry: LeagueEntryDTO) {
    Text("League Id: ${leagueEntry.leagueId}")
    Text("Summoner Id: ${leagueEntry.summonerId}")
    Text("Summoner Name: ${leagueEntry.summonerName}")
    Text("Queue Type: ${leagueEntry.queueType}")
    Text("Tier: ${leagueEntry.tier}")
    Text("Rank: ${leagueEntry.rank}")
    Text("League Points: ${leagueEntry.leaguePoints}")
    Text("Wins: ${leagueEntry.wins}")
    Text("Losses: ${leagueEntry.losses}")
    Text("Hot Streak: ${leagueEntry.hotStreak}")
    Text("Veteran: ${leagueEntry.veteran}")
    Text("Fresh Blood: ${leagueEntry.freshBlood}")
    Text("Inactive: ${leagueEntry.inactive}")
    leagueEntry.miniSeries?.let {
        Text("Mini Series Wins: ${it.wins}")
        Text("Mini Series Losses: ${it.losses}")
        Text("Mini Series Target: ${it.target}")
        Text("Mini Series Progress: ${it.progress}")
    }
}

@Composable
private fun MatchView(
    modifier: Modifier = Modifier,
    itemList: MutableList<MatchState.Success>,
    scrollState: ScrollState
) {
    Column(modifier = modifier.verticalScroll(scrollState)) {
        itemList.forEach {
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
    TopScrollContent(
        scrollState = ScrollState(0),
        leagueEntryDTO = dummy,
        summonerDTO = dummySummonerDTO
    )
}

@Preview(heightDp = 300)
@Composable
fun SummonerViewPreview() {
    SummonerView(leagueEntry = dummy, modifier = Modifier, scrollState = ScrollState(0))
}

@Preview
@Composable
fun MatchViewPreview() {
    MatchView(itemList = mutableListOf(), scrollState = ScrollState(0))
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
