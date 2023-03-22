package yunho.compose.data.entity

data class LeagueEntryEntity(
    val leagueId: String,
    val summonerId: String,
    val summonerName: String,
    val queueType: String,
    val tier: String,
    val rank: String,
    val leaguePoints: Int,
    val wins: Int,
    val losses: Int,
    val hotStreak: Boolean,
    val veteran: Boolean,
    val freshBlood: Boolean,
    val inactive: Boolean,
    val miniSeries: MiniSeriesEntity?
)
