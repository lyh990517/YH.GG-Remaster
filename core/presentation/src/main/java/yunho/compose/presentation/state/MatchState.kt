package yunho.compose.yhgg_remaster.presentaion.state

import kotlinx.coroutines.flow.Flow
import yunho.compose.domain.model.MatchDTO


sealed class MatchState{
    object Loading : MatchState()

    data class LoadIds(val ids: List<String>) : MatchState()

    data class Success(val matchData: Flow<MatchDTO>) : MatchState()

    data class Error(val e: Throwable) : MatchState()
}

