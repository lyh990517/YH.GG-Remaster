package yunho.compose.domain.repository

import kotlinx.coroutines.flow.Flow
import yunho.compose.domain.model.LeagueEntryDTO
import yunho.compose.domain.model.SummonerDTO

interface SummonerRepository {
    suspend fun getSummonerInfo(summonerName: String): Flow<SummonerDTO>

    suspend fun getSummonerLeagueEntry(id: String): Flow<List<LeagueEntryDTO>>
}