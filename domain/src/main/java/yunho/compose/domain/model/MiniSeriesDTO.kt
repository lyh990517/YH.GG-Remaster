package yunho.compose.domain.model

data class MiniSeriesDTO(
    val losses: Int,
    val progress: String,
    val target: Int,
    val wins: Int
)