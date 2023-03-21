package yunho.compose.yhgg_remaster.data.repository

import kotlinx.coroutines.flow.Flow
import yunho.compose.yhgg_remaster.data.entity.MatchDTO

interface MatchRepository {
    suspend fun getMatchId(puuid: String): Flow<List<String>>

    suspend fun getMatchInfo(matchId: String): Flow<MatchDTO>
}