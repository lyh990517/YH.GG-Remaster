package yunho.compose.data.entity

data class InfoEntity(
    val gameCreation: Long,
    val gameDuration: Long,
    val gameEndTimestamp: Long?,
    val gameId: Long,
    val gameMode: String,
    val gameName: String,
    val gameStartTimestamp: Long,
    val gameType: String,
    val gameVersion: String,
    val mapId: Int,
    val participants: List<ParticipantEntity>,
    val platformId: String,
    val queueId: Int,
    val teams: List<TeamEntity>,
    val tournamentCode: String?
)