package yunho.compose.yhgg_remaster.data.entity

data class InfoDTO(
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
    val participants: List<ParticipantDTO>,
    val platformId: String,
    val queueId: Int,
    val teams: List<TeamDTO>,
    val tournamentCode: String?
)