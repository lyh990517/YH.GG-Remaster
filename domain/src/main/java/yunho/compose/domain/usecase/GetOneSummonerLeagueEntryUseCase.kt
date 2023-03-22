package yunho.compose.domain.usecase

import yunho.compose.domain.repository.SummonerRepository
import javax.inject.Inject

class GetOneSummonerLeagueEntryUseCase @Inject constructor(private val summonerRepository: SummonerRepository) {
    suspend fun getLeagueEntry(id: String) = summonerRepository.getSummonerLeagueEntry(id)
}