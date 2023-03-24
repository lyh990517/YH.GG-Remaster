package yunho.compose.domain.usecase

import yunho.compose.domain.repository.SummonerRepository
import javax.inject.Inject

class GetOneSummonerInfoUseCase @Inject constructor(private val summonerRepository: SummonerRepository) {
    suspend fun getInfo(summonerName: String) = summonerRepository.getSummonerInfo(summonerName)
}