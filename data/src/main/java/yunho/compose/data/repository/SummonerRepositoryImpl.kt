package yunho.compose.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import yunho.compose.domain.model.LeagueEntryDTO
import yunho.compose.domain.model.SummonerDTO
import yunho.compose.domain.repository.SummonerRepository
import yunho.compose.yhgg_remaster.data.api.SummonerDataSource
import javax.inject.Inject

class SummonerRepositoryImpl @Inject constructor(private val summonerDataSource: SummonerDataSource) :
    SummonerRepository {
    override suspend fun getSummonerInfo(summonerName: String): Flow<SummonerDTO> = flow {
        val response = summonerDataSource.getSummonerInfoByName(summonerName)
        response.body()?.let {
            emit(it)
        }
    }

    override suspend fun getSummonerLeagueEntry(id: String): Flow<List<LeagueEntryDTO>> = flow {
        val response = summonerDataSource.getSummonerEntriesByEncryptedSummonerID(id)
        response.body()?.let {
            emit(it)
        }
    }
}