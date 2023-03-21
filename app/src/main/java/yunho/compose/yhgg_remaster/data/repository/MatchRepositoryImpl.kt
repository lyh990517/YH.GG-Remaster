package yunho.compose.yhgg_remaster.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import yunho.compose.yhgg_remaster.data.api.MatchService
import yunho.compose.yhgg_remaster.data.entity.MatchDTO
import javax.inject.Inject

class MatchRepositoryImpl @Inject constructor(private val matchApi: MatchService) :
    MatchRepository {
    override suspend fun getMatchId(puuid: String): Flow<List<String>> = flow {
        val response = matchApi.getMatchIdBypuuid(puuid, 1, 10)
        response.body()?.let {
            emit(it)
        }
    }

    override suspend fun getMatchInfo(matchId: String): Flow<MatchDTO> = flow {
        val response = matchApi.getMatchInfoByMatchID(matchId)
        response.body()?.let {
            emit(it)
        }
    }
}