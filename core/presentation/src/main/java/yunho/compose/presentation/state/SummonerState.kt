package yunho.compose.yhgg_remaster.presentaion.state

import kotlinx.coroutines.flow.Flow
import yunho.compose.domain.model.LeagueEntryDTO
import yunho.compose.domain.model.SummonerDTO

sealed class SummonerState {
    object Loading : SummonerState()
    data class LoadLeagueEntry(val data: Flow<List<LeagueEntryDTO>>) : SummonerState()
    data class Success(val infoData: SummonerDTO) : SummonerState()
    data class Error(val e: Throwable) : SummonerState()
}
