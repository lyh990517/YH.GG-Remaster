package yunho.compose.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import yunho.compose.domain.model.MatchDTO
import yunho.compose.domain.repository.MatchRepository
import yunho.compose.yhgg_remaster.data.api.MatchDataSource
import javax.inject.Inject

class MatchRepositoryImpl @Inject constructor(private val matchDataSource: MatchDataSource) :
    MatchRepository {
    override suspend fun getMatchId(puuid: String): Flow<List<String>> = flow {
        val response = matchDataSource.getMatchIdBypuuid(puuid, 1, 10)
        response.body()?.let {
            emit(it)
        }
    }

    override suspend fun getMatchInfo(matchId: String): Flow<MatchDTO> = flow {
        val response = matchDataSource.getMatchInfoByMatchID(matchId)
        response.body()?.let {
            emit(it)
        }
    }
}