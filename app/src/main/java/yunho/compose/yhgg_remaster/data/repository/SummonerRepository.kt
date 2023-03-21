package yunho.compose.yhgg_remaster.data.repository

import kotlinx.coroutines.flow.Flow
import yunho.compose.yhgg_remaster.data.entity.LeagueEntryDTO
import yunho.compose.yhgg_remaster.data.entity.SummonerDTO

interface SummonerRepository {
    suspend fun getSummonerInfo(summonerName: String): Flow<SummonerDTO>

    suspend fun getSummonerLeagueEntry(id: String): Flow<List<LeagueEntryDTO>>
}