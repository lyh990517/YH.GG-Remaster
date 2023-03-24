package yunho.compose.data.entity

data class SummonerEntity(
    val accountId: String,
    val profileIconId: Int,
    val revisionDate: Long,
    val name: String,
    val id: String,
    val puuid: String,
    val summonerLevel: Long
)
