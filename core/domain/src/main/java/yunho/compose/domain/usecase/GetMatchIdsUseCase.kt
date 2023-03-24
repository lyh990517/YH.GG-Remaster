package yunho.compose.domain.usecase

import yunho.compose.domain.repository.MatchRepository
import javax.inject.Inject

class GetMatchIdsUseCase @Inject constructor(private val matchRepository: MatchRepository) {
    suspend fun getMatchIds(puuid: String) = matchRepository.getMatchId(puuid)
}