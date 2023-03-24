package yunho.compose.domain.model

data class MetadataDTO(
    val dataVersion: String,
    val matchId: String,
    val participants: List<String>
)