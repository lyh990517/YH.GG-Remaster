package yunho.compose.yhgg_remaster.presentaion.state

import yunho.compose.domain.model.LeagueEntryDTO
import yunho.compose.domain.model.SummonerDTO

sealed class SummonerState {
    object UnInitialized : SummonerState()
    object Loading : SummonerState()
    data class LoadLeagueEntry(val data: List<LeagueEntryDTO>) : SummonerState()
    data class Success(val infoData: SummonerDTO) : SummonerState()
    data class Error(val e: Throwable) : SummonerState()
}
