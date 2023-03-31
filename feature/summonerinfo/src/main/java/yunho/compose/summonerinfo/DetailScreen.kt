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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
                navigator = navigator,
                scrollState = scrollState,
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
    navigator: NavController,
    scrollState: ScrollState,
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
            painter = painterResource(id = R.drawable.background2),
            contentDescription = "backGround",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .heightIn(max = animateDpAsState(targetValue = dynamicHeight.dp).value)
                .fillMaxSize(),
        )
        Column(
            Modifier
                .heightIn(
                    max = animateDpAsState(
                        targetValue = dynamicHeight.dp
                    ).value
                )
                .fillMaxSize(), verticalArrangement = Arrangement.Bottom
        ) {
            Column(Modifier) {
                Row(modifier = Modifier) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_icon),
                        contentDescription = "profile Image",
                        Modifier
                            .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 15.dp)
                            .width(100.dp)
                            .height(100.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = summonerDTO.name,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(top = 15.dp),
                        fontStyle = FontStyle.Normal,
                        color = Color.White
                    )
                }
                if (dynamicHeight.dp != 130.dp) Text(
                    text = "${summonerDTO.summonerLevel}",
                    modifier = Modifier.padding(start = 50.dp, bottom = 5.dp),
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
        if (dynamicHeight.dp != 130.dp) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .padding(15.dp)
                    .clickable {
                        navigator.popBackStack()
                    })
        }
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
        LazyRow(
            modifier
                .heightIn(
                    max = animateDpAsState(
                        targetValue = dynamicHeight.dp
                    ).value
                )
                .fillMaxWidth()
                .background(Color(ContextCompat.getColor(LocalContext.current, R.color.rank_view))),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items(leagueEntry) {
                RankItem(leagueEntry = it)
            }
        }
    }
}

@Composable
fun RankItem(leagueEntry: LeagueEntryDTO) {
    Column(
        Modifier
            .padding(3.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(width = 1.dp, color = Color.Cyan, shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(
            Modifier
                .padding(horizontal = 5.dp)
                .fillMaxWidth()
        ) {
            Column(Modifier.padding(vertical = 5.dp, horizontal = 10.dp)) {
                Image(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp),
                    painter = painterResource(id = R.drawable.emblem_diamond),
                    contentDescription = "tier"
                )
                Text(leagueEntry.queueType)
            }
            Column(
                Modifier.padding(vertical = 5.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text("Tier: ${leagueEntry.tier}")
                Text("Rank: ${leagueEntry.rank}")
                Text("League Points: ${leagueEntry.leaguePoints}")
                Text("Wins: ${leagueEntry.wins}")
                Text("Losses: ${leagueEntry.losses}")
            }
        }
    }
}

@Preview
@Composable
fun RankItemPreview() {
    RankItem(leagueEntry = dummy[0])
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
        navigator = rememberNavController(),
        scrollState = ScrollState(0),
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
