package yunho.compose.yhgg_remaster.domain

import yunho.compose.yhgg_remaster.data.repository.SummonerRepository
import javax.inject.Inject

class GetOneSummonerLeagueEntryUseCase @Inject constructor(private val summonerRepository: SummonerRepository) {
    suspend fun getLeagueEntry(id: String) = summonerRepository.getSummonerLeagueEntry(id)
}