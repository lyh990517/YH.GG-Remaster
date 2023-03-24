package yunho.compose.data.entity

data class TeamEntity(
    val bans: List<BanEntity>,
    val objectives: ObjectivesEntity,
    val teamId: Int,
    val win: Boolean
)