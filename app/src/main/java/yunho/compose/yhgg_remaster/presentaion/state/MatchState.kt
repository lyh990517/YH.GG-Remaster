package yunho.compose.yhgg_remaster.presentaion.state

import yunho.compose.yhgg_remaster.data.entity.MatchDTO


sealed class MatchState{
    object Loading : MatchState()

    data class LoadIds(val ids: List<String>) : MatchState()

    data class Success(val matchData: MatchDTO) : MatchState()

    data class Error(val e: Throwable) : MatchState()
}

