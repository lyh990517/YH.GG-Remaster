package yunho.compose.yhgg_remaster.data.entity

data class MetadataDTO(
    val dataVersion: String,
    val matchId: String,
    val participants: List<String>
)