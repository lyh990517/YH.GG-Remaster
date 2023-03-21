package yunho.compose.yhgg_remaster.domain

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import yunho.compose.yhgg_remaster.data.repository.SummonerRepository
import javax.inject.Inject

class GetOneSummonerInfoUseCase @Inject constructor(private val summonerRepository: SummonerRepository) {
    suspend fun getInfo(summonerName: String) = summonerRepository.getSummonerInfo(summonerName)
}