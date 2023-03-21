package yunho.compose.yhgg_remaster.data.entity

data class PerkStyleDTO(
    val description: String,
    val selections: List<PerkStyleSelectionDTO>,
    val style: Int
)