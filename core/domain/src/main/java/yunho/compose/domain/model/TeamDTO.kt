package yunho.compose.domain.model

data class TeamDTO(
    val bans: List<BanDTO>,
    val objectives: ObjectivesDTO,
    val teamId: Int,
    val win: Boolean
)