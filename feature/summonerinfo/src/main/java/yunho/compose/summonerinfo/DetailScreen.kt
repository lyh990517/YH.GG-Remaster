package yunho.compose.summonerinfo

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
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
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
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
            summonerLeague.value = leagueEntryDTO.data.ifEmpty { listOf() }
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
                summonerDTO = currentSummoner.value,
                matchList = matchList,
                matchState = matchState
            )
            RankView(
                Modifier, leagueEntry = summonerLeague.value, scrollState = scrollState
            )
            MatchView(
                Modifier.weight(1f),
                itemList = matchList,
                scrollState = scrollState,
                summonerDTO = currentSummoner.value,
                matchState = matchState
            )
        }
    }
    BackHandler {
        navigator.popBackStack()
        matchList.clear()
    }
}

@Composable
private fun MatchView(
    modifier: Modifier = Modifier,
    itemList: MutableList<MatchState.Success>,
    scrollState: ScrollState,
    summonerDTO: SummonerDTO,
    matchState: MatchState
) {
    if (matchState is MatchState.Loading) return
    Column(modifier = modifier.verticalScroll(scrollState)) {
        itemList.forEach {
            MatchItem(it.matchData, summonerDTO)
        }
    }
}

@Composable
fun MatchItem(matchData: MatchDTO, summonerDTO: SummonerDTO) {
    if (matchData.info.participants.none { it.summonerName == summonerDTO.name }) return
    val myData = matchData.info.participants.filter { it.summonerName == summonerDTO.name }[0]
    val isExpand = rememberSaveable { mutableStateOf(false) }
    val color = LocalContext.current.getColor(if (myData.win) R.color.win else R.color.lose)
    val colorBack =
        LocalContext.current.getColor(if (myData.win) R.color.win_back else R.color.lose_back)
    val item =
        LocalContext.current.getString(R.string.ITEM_IMAGE_BASE_URL)
    val item0 = item + "${myData.item0}.png"
    val item1 = item + "${myData.item1}.png"
    val item2 = item + "${myData.item2}.png"
    val item3 = item + "${myData.item3}.png"
    val item4 = item + "${myData.item4}.png"
    val item5 = item + "${myData.item5}.png"
    val item6 = item + "${myData.item6}.png"
    val champ = LocalContext.current.getString(R.string.CHAMPION_SQUARE_IMAGE_BASE_URL)

    Column(
        Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = Color(colorBack))
    ) {
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.White)
        )
        Row(
            Modifier
                .height(100.dp)
                .fillMaxWidth()
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .width(40.dp)
                    .background(Color(color)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = if (myData.win) "승리" else "패배", color = Color.White, fontSize = 12.sp)
                Spacer(
                    modifier = Modifier
                        .padding(7.dp)
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color.White)
                )
            }
            Column(
                Modifier
                    .fillMaxSize(), verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    Modifier
                        .padding(5.dp)
                        .wrapContentSize()) {
                    AsyncImage(
                        model = champ + "${myData.championName}.png",
                        contentDescription = "champ"
                    )
                }
                Row(
                    Modifier
                        .padding(5.dp)
                        .wrapContentSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val modi = Modifier
                        .padding(2.dp)
                        .width(30.dp)
                        .height(30.dp)
                    AsyncImage(
                        modifier = modi,
                        model = item0,
                        contentDescription = "item 0"
                    )
                    AsyncImage(
                        modifier = modi,
                        model = item1,
                        contentDescription = "item 1"
                    )
                    AsyncImage(
                        modifier = modi,
                        model = item2,
                        contentDescription = "item 2"
                    )
                    AsyncImage(
                        modifier = modi,
                        model = item3,
                        contentDescription = "item 3"
                    )
                    AsyncImage(
                        modifier = modi,
                        model = item4,
                        contentDescription = "item 4"
                    )
                    AsyncImage(
                        modifier = modi,
                        model = item5,
                        contentDescription = "item 5"
                    )
                    AsyncImage(
                        modifier = modi,
                        model = item6,
                        contentDescription = "item 6"
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.White)
        )
    }
}

@Composable
fun TopScrollContent(
    navigator: NavController,
    scrollState: ScrollState,
    summonerDTO: SummonerDTO,
    matchList: List<MatchState.Success>,
    matchState: MatchState
) {
    if (matchState is MatchState.Loading || matchList.isEmpty()) return
    val dynamicHeight = (250f - scrollState.value).coerceIn(130f, 250f)
    val scope = rememberCoroutineScope()
    val modifier = Modifier
        .heightIn(min = animateDpAsState(targetValue = dynamicHeight.dp).value)
        .fillMaxWidth()
    val profile =
        LocalContext.current.getString(R.string.PROFILE_ICON_BASE_URL) + "${summonerDTO.profileIconId}.png"
    val data =
        if (matchList.isNotEmpty()) LocalContext.current.getString(R.string.CHAMPION_IMAGE_BASE_URL) +
                "${matchList[0].matchData.info.participants[0].championName}_0.jpg" else ""
    val background = remember {
        mutableStateOf(data)
    }
    background.value = data
    BoxWithConstraints(
        modifier = modifier
    ) {
        AsyncImage(
            model = background.value,
            contentDescription = "backGround",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .heightIn(max = animateDpAsState(targetValue = dynamicHeight.dp).value)
                .fillMaxSize(),
            onError = {
                Log.e("error", "${it.result.throwable.message}")
            }
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
                    AsyncImage(
                        model = profile,
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
                        fontFamily = font_t,
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
            Icon(imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .padding(15.dp)
                    .clickable {
                        navigator.popBackStack()
                    })
            Icon(imageVector = Icons.Default.Star,
                contentDescription = "",
                tint = Color.Yellow,
                modifier = Modifier
                    .padding(15.dp)
                    .align(Alignment.TopEnd)
                    .clickable {
                        //즐겨찾기
                    })
        }
        if (dynamicHeight.dp == 130.dp) {
            Icon(imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .padding(15.dp)
                    .align(Alignment.BottomEnd)
                    .clickable {
                        scope.launch {
                            scrollState.scrollTo(0)
                        }
                    })
        }

    }
}

@Composable
fun RankView(
    modifier: Modifier, leagueEntry: List<LeagueEntryDTO>, scrollState: ScrollState
) {
    val dynamicHeight = (120f - scrollState.value).coerceIn(0f, 120f)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(LocalContext.current.getColor(R.color.spacer)))
            .heightIn(
                min = animateDpAsState(
                    targetValue = dynamicHeight.dp
                ).value
            )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Color(LocalContext.current.getColor(R.color.spacer)))
                .heightIn(
                    min = animateDpAsState(
                        targetValue = dynamicHeight.dp
                    ).value
                )
        ) {
            LazyRow(
                modifier
                    .heightIn(
                        max = animateDpAsState(targetValue = dynamicHeight.dp).value
                    )
                    .fillMaxWidth()
                    .background(Color(LocalContext.current.getColor(R.color.spacer))),
                horizontalArrangement = Arrangement.Start
            ) {
                when {
                    leagueEntry.size == 1 && leagueEntry[0].queueType == "RANKED_SOLO_5x5" -> {
                        val rankList = listOf(leagueEntry[0], dummy[1])
                        items(rankList) {
                            RankItem(
                                leagueEntry = it,
                                modifier
                                    .heightIn(
                                        max = animateDpAsState(targetValue = dynamicHeight.dp).value
                                    )
                                    .fillMaxSize()
                            )
                        }
                    }
                    leagueEntry.size == 1 && leagueEntry[0].queueType != "RANKED_SOLO_5x5" -> {
                        val rankList = listOf(leagueEntry[0], dummy[0])
                        items(rankList) {
                            RankItem(
                                leagueEntry = it,
                                modifier
                                    .heightIn(
                                        max = animateDpAsState(targetValue = dynamicHeight.dp).value
                                    )
                                    .fillMaxSize()
                            )
                        }
                    }
                    leagueEntry.isEmpty() -> {
                        items(dummy) {
                            RankItem(
                                leagueEntry = it,
                                modifier
                                    .heightIn(
                                        max = animateDpAsState(targetValue = dynamicHeight.dp).value
                                    )
                                    .fillMaxSize()
                            )
                        }
                    }
                    leagueEntry.size == 2 -> {
                        items(leagueEntry) {
                            RankItem(
                                leagueEntry = it,
                                modifier
                                    .heightIn(
                                        max = animateDpAsState(targetValue = dynamicHeight.dp).value
                                    )
                                    .fillMaxSize()
                            )
                        }
                    }
                }
            }
        }

    }
}

val font = FontFamily(
    Font(R.font.font)
)

val font_t = FontFamily(
    Font(R.font.font_thick)
)

@Composable
fun RankItem(leagueEntry: LeagueEntryDTO, modifier: Modifier = Modifier) {
    val rankType = if (leagueEntry.queueType == "RANKED_SOLO_5x5") "개인/2인 랭크" else "자유 랭크"
    val rankImage = when (leagueEntry.tier) {
        "IRON" -> R.drawable.iron
        "BRONZE" -> R.drawable.bronze
        "SILVER" -> R.drawable.silver
        "GOLD" -> R.drawable.gold
        "PLATINUM" -> R.drawable.platnum
        "DIAMOND" -> R.drawable.diamond
        "MASTER" -> R.drawable.master
        "GRANDMASTER" -> R.drawable.grandmaster
        "CHALLENGER" -> R.drawable.challenger
        else -> R.drawable.unrank
    }
    Column(
        modifier = modifier
            .padding(3.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = Color(ContextCompat.getColor(LocalContext.current, R.color.rank_view)),
                shape = RoundedCornerShape(20.dp)
            )
            .background(Color.White)
            .fillMaxSize(), verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier
                .padding(horizontal = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                Modifier
                    .padding(vertical = 5.dp, horizontal = 10.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Image(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp),
                    painter = painterResource(id = rankImage),
                    contentDescription = "tier"
                )
            }
            Column(
                Modifier
                    .padding(10.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Badge(backgroundColor = Color(LocalContext.current.getColor(R.color.badge))) {
                    Text(
                        rankType,
                        fontFamily = font,
                        color = Color(LocalContext.current.getColor(R.color.badge_text))
                    )
                }
                Text(
                    "${leagueEntry.tier} ${leagueEntry.rank}",
                    Modifier.padding(end = 15.dp),
                    fontFamily = font_t,
                    fontSize = 18.sp
                )
                Text("LP: ${leagueEntry.leaguePoints}", fontFamily = font)
                Row(Modifier, horizontalArrangement = Arrangement.Center) {
                    Text("${leagueEntry.wins}:승", fontFamily = font)
                    Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                    Text("${leagueEntry.losses}:패", fontFamily = font)
                }
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
fun TopImageView(modifier: Modifier = Modifier, summonerDTO: SummonerDTO) {
}

@Composable
fun RankView(modifier: Modifier = Modifier) {

}

@Preview
@Composable
fun MatchItemPreview() {

}

@Preview
@Composable
fun TopImageViewPreview() {
//    TopScrollContent(
//        navigator = rememberNavController(),
//        scrollState = ScrollState(0),
//        summonerDTO = dummySummonerDTO
//    )
}

@Preview(heightDp = 300)
@Composable
fun SummonerViewPreview() {
    RankView(leagueEntry = dummy, modifier = Modifier, scrollState = ScrollState(0))
}

@Preview
@Composable
fun MatchViewPreview() {
    MatchView(
        itemList = mutableListOf(),
        scrollState = ScrollState(0),
        summonerDTO = dummySummonerDTO,
        matchState = MatchState.Loading
    )
}

val dummy = listOf(
    LeagueEntryDTO(
        leagueId = "",
        summonerId = "",
        summonerName = "",
        queueType = "RANKED_SOLO_5x5",
        tier = "UNRANK",
        rank = "",
        leaguePoints = 0,
        wins = 0,
        losses = 0,
        hotStreak = false,
        veteran = false,
        freshBlood = false,
        inactive = false,
        miniSeries = null
    ), LeagueEntryDTO(
        leagueId = "",
        summonerId = "",
        summonerName = "",
        queueType = "RANKED_FLEX_5x5",
        tier = "UNRANK",
        rank = "",
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
