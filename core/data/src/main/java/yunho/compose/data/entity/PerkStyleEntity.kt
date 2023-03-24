package yunho.compose.data.entity

data class PerkStyleEntity(
    val description: String,
    val selections: List<PerkStyleSelectionEntity>,
    val style: Int
)