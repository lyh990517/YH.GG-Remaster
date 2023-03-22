package yunho.compose.data.entity

data class MetadataEntity(
    val dataVersion: String,
    val matchId: String,
    val participants: List<String>
)