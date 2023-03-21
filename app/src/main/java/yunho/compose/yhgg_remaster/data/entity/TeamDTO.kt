package yunho.compose.yhgg_remaster.data.entity

data class TeamDTO(
    val bans: List<BanDTO>,
    val objectives: ObjectivesDTO,
    val teamId: Int,
    val win: Boolean
)