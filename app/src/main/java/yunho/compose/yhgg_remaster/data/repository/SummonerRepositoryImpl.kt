package yunho.compose.yhgg_remaster.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import yunho.compose.yhgg_remaster.data.api.SummonerService
import yunho.compose.yhgg_remaster.data.entity.LeagueEntryDTO
import yunho.compose.yhgg_remaster.data.entity.SummonerDTO
import javax.inject.Inject

class SummonerRepositoryImpl @Inject constructor(private val summonerApi: SummonerService) :
    SummonerRepository {
    override suspend fun getSummonerInfo(summonerName: String): Flow<SummonerDTO> = flow {
        val response = summonerApi.getSummonerInfoByName(summonerName)
        response.body()?.let {
            emit(it)
        }
    }

    override suspend fun getSummonerLeagueEntry(id: String): Flow<List<LeagueEntryDTO>> = flow {
        val response = summonerApi.getSummonerEntriesByEncryptedSummonerID(id)
        response.body()?.let {
            emit(it)
        }
    }
}