package yunho.compose.domain.repository

import kotlinx.coroutines.flow.Flow
import yunho.compose.domain.model.MatchDTO

interface MatchRepository {
    suspend fun getMatchId(puuid: String): Flow<List<String>>

    suspend fun getMatchInfo(matchId: String): Flow<MatchDTO>
}