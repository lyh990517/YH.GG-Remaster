package yunho.compose.yhgg_remaster.domain

import yunho.compose.yhgg_remaster.data.repository.MatchRepository
import javax.inject.Inject

class GetMatchIdsUseCase @Inject constructor(private val matchRepository: MatchRepository) {
    suspend fun getMatchIds(puuid: String) = matchRepository.getMatchId(puuid)
}