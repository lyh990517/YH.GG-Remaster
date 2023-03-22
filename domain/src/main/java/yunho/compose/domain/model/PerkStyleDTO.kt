package yunho.compose.domain.model

data class PerkStyleDTO(
    val description: String,
    val selections: List<PerkStyleSelectionDTO>,
    val style: Int
)